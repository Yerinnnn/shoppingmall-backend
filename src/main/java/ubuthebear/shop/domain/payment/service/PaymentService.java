package ubuthebear.shop.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.repository.MemberRepository;
import ubuthebear.shop.domain.payment.dto.*;
import ubuthebear.shop.domain.payment.entity.Payment;
import ubuthebear.shop.domain.payment.entity.PaymentHistory;
import ubuthebear.shop.domain.payment.entity.PaymentStatus;

import ubuthebear.shop.domain.payment.repository.PaymentRepository;
import ubuthebear.shop.domain.payment.repository.PaymentHistoryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final MemberRepository memberRepository;

    @Value("${toss.payments.secret.key}")
    private String secretKey;

    @Value("${toss.payments.client.key}")
    private String clientKey;

    private static final String TOSS_PAYMENTS_API_URL = "https://api.tosspayments.com/v1";

    /**
     * 결제 준비 (클라이언트 키 발급)
     */
    @Transactional
    public PaymentPrepareResponse preparePayment(String username, PaymentPrepareRequest request) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        log.info("Preparing payment for order ID: {}, amount: {}, username: {}",
                request.getOrderId(), request.getAmount(), username);

        // 주문번호 중복 체크
        if (paymentRepository.findByOrderId(request.getOrderId()).isPresent()) {
            throw new RuntimeException("Duplicate order ID");
        }

        // 결제 정보 생성
        Payment payment = Payment.createPayment(member, request.getOrderId(), request.getAmount());
        paymentRepository.save(payment);

        // 결제 이력 생성
        createPaymentHistory(payment, "결제가 시작되었습니다");

        return new PaymentPrepareResponse(clientKey, request.getOrderId());
    }

    /**
     * 결제 승인 처리
     */
    @Transactional
    public PaymentResponse confirmPayment(PaymentConfirmRequest request) {
        // 1. 결제 정보 조회
        Payment payment = paymentRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // 2. 결제 상태 확인
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Invalid payment status: " + payment.getStatus());
        }

        // 3. 결제 금액 검증
        if (!payment.getAmount().equals(request.getAmount())) {
            throw new RuntimeException("Amount mismatch. Expected: " + payment.getAmount() + ", Actual: " + request.getAmount());
        }

        try {
            // 4. 토스페이먼츠 결제 승인 API 호출
            String authHeader = generateAuthHeader();
            JSONObject requestBody = new JSONObject()
                    .put("paymentKey", request.getPaymentKey())
                    .put("orderId", request.getOrderId())
                    .put("amount", request.getAmount());

            JSONObject response = callTossPaymentsAPI("/payments/confirm", "POST", authHeader, requestBody);

            // 5. 결제 정보 업데이트
            JSONObject cardInfo = response.getJSONObject("card");
            PaymentSuccessDetail detail = PaymentSuccessDetail.builder()
                    .paymentMethod(response.getString("method"))
                    .cardNumber(cardInfo.getString("number"))
                    .cardCompany(cardInfo.getString("company"))
                    .installmentPlanMonths(cardInfo.optInt("installmentPlanMonths", 0))
                    .approveNo(cardInfo.optString("approveNo"))
                    .cardType(cardInfo.optString("cardType"))
                    .ownerType(cardInfo.optString("ownerType"))
                    .acquireStatus(cardInfo.optString("acquireStatus"))
                    .useCardPoint(cardInfo.optBoolean("useCardPoint"))
                    .build();

            payment.markAsComplete(request.getPaymentKey(), detail);
            createPaymentHistory(payment, "결제가 완료되었습니다");

            return new PaymentResponse(payment);
        } catch (Exception e) {
            // 6. 실패 처리
            payment.markAsFailed(e.getMessage());
            createPaymentHistory(payment, "결제 실패: " + e.getMessage());
            throw new RuntimeException("Payment confirmation failed: " + e.getMessage());
        }
    }

    /**
     * 결제 취소
     */
    @Transactional
    public PaymentResponse cancelPayment(String username, Long paymentId, PaymentCancelRequest request) {
        // 1. 결제 정보 조회 및 권한 확인
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        // 2. 결제 상태 확인
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Payment cannot be cancelled. Current status: " + payment.getStatus());
        }

        try {
            // 3. 토스페이먼츠 결제 취소 API 호출
            String authHeader = generateAuthHeader();
            JSONObject requestBody = new JSONObject()
                    .put("cancelReason", request.getCancelReason());

            callTossPaymentsAPI("/payments/" + payment.getPaymentKey() + "/cancel", "POST", authHeader, requestBody);

            // 4. 결제 정보 업데이트
            payment.markAsCancelled(request.getCancelReason());
            createPaymentHistory(payment, "결제 취소: " + request.getCancelReason());

            return new PaymentResponse(payment);
        } catch (Exception e) {
            log.error("Payment cancellation failed:", e);
            throw new RuntimeException("Payment cancellation failed: " + e.getMessage());
        }
    }

    /**
     * 결제 상세 정보 조회
     */
    public PaymentResponse getPayment(String username, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        return new PaymentResponse(payment);
    }

    /**
     * 내 결제 내역 조회
     */
    public List<PaymentResponse> getMyPayments(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return paymentRepository.findByMemberOrderByCreatedAtDesc(member).stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 결제 이력 조회
     */
    public List<PaymentHistoryResponse> getPaymentHistory(String username, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        return paymentHistoryRepository.findByPaymentOrderByCreatedAtDesc(payment).stream()
                .map(PaymentHistoryResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 결제 이력 생성 헬퍼 메서드
     */
    private void createPaymentHistory(Payment payment, String description) {
        PaymentHistory history = PaymentHistory.createHistory(payment, description);
        paymentHistoryRepository.save(history);
    }

    /**
     * 토스페이먼츠 API 인증 헤더 생성
     */
    private String generateAuthHeader() {
        String credentials = secretKey + ":";
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 토스페이먼츠 API 호출 메서드
     */
    private JSONObject callTossPaymentsAPI(String path, String method, String authHeader, JSONObject requestBody) throws Exception {
        URL url = new URL(TOSS_PAYMENTS_API_URL + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod(method);
            conn.setRequestProperty("Authorization", authHeader);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // 요청 바디 전송
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 응답 처리
            int responseCode = conn.getResponseCode();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? conn.getInputStream() : conn.getErrorStream(),
                            StandardCharsets.UTF_8))) {
                String response = br.lines().collect(Collectors.joining("\n"));

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return new JSONObject(response);
                } else {
                    throw new RuntimeException("API call failed: " + response);
                }
            }
        } finally {
            conn.disconnect();
        }
    }
}
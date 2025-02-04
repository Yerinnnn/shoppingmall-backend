package ubuthebear.shop.domain.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.payment.dto.*;
import ubuthebear.shop.domain.payment.service.PaymentService;

import java.util.List;

@Tag(name = "Payment", description = "결제 관리 API")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 준비 API
     * 클라이언트 키와 주문 정보를 반환
     */
    @Operation(summary = "결제 준비", description = "결제 준비 및 클라이언트 키 발급")
    @PostMapping("/prepare")
    public ResponseEntity<PaymentPrepareResponse> preparePayment(
            Authentication authentication,
            @Valid @RequestBody PaymentPrepareRequest request
    ) {
        log.info("Payment preparation requested - orderId: {}, amount: {}",
                request.getOrderId(), request.getAmount());

        try {
            PaymentPrepareResponse response = paymentService.preparePayment(
                    authentication.getName(),
                    request
            );

            log.info("Payment preparation successful - orderId: {}", request.getOrderId());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Payment preparation failed - orderId: {}, error: {}",
                    request.getOrderId(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 결제 승인 API
     * 클라이언트로부터 받은 결제 정보로 최종 결제를 승인
     */
    @Operation(summary = "결제 승인", description = "결제 승인 처리")
    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(
            @Valid @RequestBody PaymentConfirmRequest request
    ) {
        return ResponseEntity.ok(paymentService.confirmPayment(request));
    }

    /**
     * 결제 취소 API
     */
    @Operation(summary = "결제 취소", description = "결제 취소 처리")
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(
            Authentication authentication,
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentCancelRequest request
    ) {
        return ResponseEntity.ok(
                paymentService.cancelPayment(authentication.getName(), paymentId, request)
        );
    }

    /**
     * 결제 상세 정보 조회 API
     */
    @Operation(summary = "결제 상세 조회", description = "결제 상세 정보 조회")
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(
            Authentication authentication,
            @PathVariable Long paymentId
    ) {
        return ResponseEntity.ok(
                paymentService.getPayment(authentication.getName(), paymentId)
        );
    }

    /**
     * 내 결제 내역 조회 API
     */
    @Operation(summary = "내 결제 내역 조회", description = "사용자의 결제 내역 조회")
    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                paymentService.getMyPayments(authentication.getName())
        );
    }

    /**
     * 결제 이력 조회 API
     */
    @Operation(summary = "결제 이력 조회", description = "특정 결제의 상태 변경 이력 조회")
    @GetMapping("/{paymentId}/history")
    public ResponseEntity<List<PaymentHistoryResponse>> getPaymentHistory(
            Authentication authentication,
            @PathVariable Long paymentId
    ) {
        return ResponseEntity.ok(
                paymentService.getPaymentHistory(authentication.getName(), paymentId)
        );
    }
}
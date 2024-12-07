package ubuthebear.shop.domain.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.payment.dto.PaymentRequest;
import ubuthebear.shop.domain.payment.dto.PaymentResponse;
import ubuthebear.shop.domain.payment.service.PaymentService;

import java.util.List;

/**
 * 결제 관련 REST API를 제공하는 컨트롤러
 * 결제 처리, 환불, 조회 등의 엔드포인트를 정의
 *
 * @author ubuthebear
 * @version 1.0
 */
@Tag(name = "Payment", description = "결제 관리 API")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제를 처리하는 API 엔드포인트
     * POST /api/payments/process
     *
     * @param authentication Spring Security 인증 객체
     * @param request 결제 처리 요청 정보
     * @return ResponseEntity<PaymentResponse> 처리된 결제 정보
     */
    @Operation(summary = "결제 처리", description = "주문에 대한 결제를 처리합니다.")
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(
            Authentication authentication,
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(authentication.getName(), request));
    }

    @Operation(summary = "결제 환불", description = "결제를 환불 처리합니다.")
    @PostMapping("/{orderId}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(
            Authentication authentication,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.refundPayment(authentication.getName(), orderId));
    }

    @Operation(summary = "결제 내역 조회", description = "결제 내역을 조회합니다.")
    @GetMapping("/history")
    public ResponseEntity<List<PaymentResponse>> getPaymentHistory(Authentication authentication) {
        return ResponseEntity.ok(paymentService.getPaymentHistory(authentication.getName()));
    }

    @Operation(summary = "결제 상세 조회", description = "특정 결제의 상세 정보를 조회합니다.")
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentDetails(
            Authentication authentication,
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentDetails(authentication.getName(), paymentId));
    }
}
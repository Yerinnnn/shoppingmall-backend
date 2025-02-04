package ubuthebear.shop.domain.payment.dto;

import lombok.Getter;
import ubuthebear.shop.domain.payment.entity.Payment;
import ubuthebear.shop.domain.payment.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 결제 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * Payment 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see Payment
 * @see PaymentStatus
 */
@Getter
public class PaymentResponse {
    private Long id;
    private String paymentKey;
    private String orderId;
    private BigDecimal amount;
    private PaymentStatus status;
    private String paymentMethod;
    private String cardNumber;
    private String cardCompany;
    private Integer installmentPlanMonths;
    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;
    private String failureReason;
    private String cancelReason;
    private LocalDateTime createdAt;

    /**
     * Payment 엔티티를 PaymentResponse DTO로 변환하는 생성자
     * 결제 정보를 클라이언트에게 제공하기 적합한 형태로 변환
     *
     * @param payment 변환할 Payment 엔티티
     */
    public PaymentResponse(Payment payment) {
        this.id = payment.getId();
        this.paymentKey = payment.getPaymentKey();
        this.orderId = payment.getOrderId();
        this.amount = payment.getAmount();
        this.status = payment.getStatus();
        this.paymentMethod = payment.getPaymentMethod();
        this.cardNumber = payment.getCardNumber();
        this.cardCompany = payment.getCardCompany();
        this.installmentPlanMonths = payment.getInstallmentPlanMonths();
        this.paidAt = payment.getPaidAt();
        this.cancelledAt = payment.getCancelledAt();
        this.failureReason = payment.getFailureReason();
        this.cancelReason = payment.getCancelReason();
        this.createdAt = payment.getCreatedAt();
    }
}

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
    private final Long paymentId;        // 결제 고유 식별자
    private final String transactionId;  // PG사 거래 고유번호
    private final BigDecimal amount;     // 결제 금액
    private final PaymentStatus status;  // 결제 상태
    private final LocalDateTime createdAt; // 결제 생성 일시

    /**
     * Payment 엔티티를 PaymentResponse DTO로 변환하는 생성자
     * 결제 정보를 클라이언트에게 제공하기 적합한 형태로 변환
     *
     * @param payment 변환할 Payment 엔티티
     */
    public PaymentResponse(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.transactionId = payment.getTransactionId();
        this.amount = payment.getAmount();
        this.status = payment.getStatus();
        this.createdAt = payment.getCreatedAt();
    }
}

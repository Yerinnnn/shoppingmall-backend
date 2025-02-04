package ubuthebear.shop.domain.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.order.entity.Order;
import ubuthebear.shop.domain.payment.dto.PaymentSuccessDetail;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // paymentKey는 결제 승인 시에 받게 되므로, nullable true로 변경
    @Column(unique = true)
    private String paymentKey;  // 토스페이먼츠 결제 키

    @Column(nullable = false, unique = true)
    private String orderId;     // 주문 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;      // 결제 회원

    @Column(nullable = false)
    private BigDecimal amount;  // 결제 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // 결제 상태

    @Column(length = 50)
    private String paymentMethod;  // 결제 수단

    private String cardNumber;     // 카드 번호 (마스킹)
    private String cardCompany;    // 카드사
    private Integer installmentPlanMonths;  // 할부 개월

    private LocalDateTime paidAt;      // 결제 완료 시간
    private String failureReason;      // 실패 사유
    private LocalDateTime cancelledAt; // 취소 시간
    private String cancelReason;      // 취소 사유

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 결제 시작 시 생성
    public static Payment createPayment(Member member, String orderId, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setMember(member);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.PENDING);
        return payment;
    }

    // 결제 성공 처리
    public void markAsComplete(String paymentKey, PaymentSuccessDetail detail) {
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.COMPLETED;
        this.paymentMethod = detail.getPaymentMethod();
        this.cardNumber = detail.getCardNumber();
        this.cardCompany = detail.getCardCompany();
        this.installmentPlanMonths = detail.getInstallmentPlanMonths();
        this.paidAt = LocalDateTime.now();
    }

    // 결제 실패 처리
    public void markAsFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    // 결제 취소 처리
    public void markAsCancelled(String reason) {
        this.status = PaymentStatus.CANCELLED;
        this.cancelReason = reason;
        this.cancelledAt = LocalDateTime.now();
    }
}
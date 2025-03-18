package ubuthebear.shop.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.entity.Address;
import ubuthebear.shop.domain.member.entity.PaymentMethod;
import ubuthebear.shop.domain.payment.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.antlr.v4.runtime.misc.IntervalSet.subtract;

/**
 * 주문 정보를 저장하는 엔티티 클래스
 * 회원의 상품 주문 정보와 배송, 결제, 포인트 관련 정보를 관리
 *
 * @author ubuthebear
 * @version 1.0
 * @see Member
 * @see OrderItem
 * @see Payment
 */
@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(unique = true)
    private String orderNumber;

    /**
     * 주문한 회원 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 주문 상품 목록
     * 주문과 함께 저장, 수정, 삭제가 이루어짐 (CascadeType.ALL)
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 배송 주소 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;

    /**
     * 결제 수단 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

//    /**
//     * 결제 정보
//     * 결제와 함께 저장, 수정, 삭제가 이루어짐 (CascadeType.ALL)
//     */
//    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
//    private Payment payment;

    /**
     * 주문 상태
     * PENDING(대기), PAID(결제완료), PREPARING(준비중),
     * SHIPPING(배송중), DELIVERED(배송완료),
     * COMPLETED(주문완료), CANCELLED(취소됨)
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    /**
     * 주문 총액
     * 상품 금액의 합계에서 사용 포인트를 차감한 최종 결제 금액
     */
    private BigDecimal totalAmount;

    /**
     * 주문 생성 일시
     * JPA Auditing을 통해 자동으로 설정
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 주문 수정 일시
     * JPA Auditing을 통해 자동으로 업데이트
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 주문 시 사용한 포인트 금액
     * 기본값은 0
     */
    private BigDecimal usedPoints = BigDecimal.ZERO;

    /**
     * 주문으로 적립 예정인 포인트 금액
     * 구매 확정 시 실제 적립됨
     * 기본값은 0
     */
    private BigDecimal earnedPoints = BigDecimal.ZERO;

    /**
     * 주문 상품 추가 메서드
     * 양방향 연관관계 설정을 처리
     *
     * @param orderItem 추가할 주문 상품
     */
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 주문 총액 계산 메서드
     * 모든 주문 상품의 소계를 합산하고 사용 포인트를 차감
     */
    public void calculateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(this.usedPoints); // 포인트 사용액 차감
    }
}
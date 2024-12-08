package ubuthebear.shop.domain.shipping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ubuthebear.shop.domain.order.entity.Order;

import java.time.LocalDateTime;

/**
 * 배송 정보를 저장하는 엔티티 클래스
 * 주문에 대한 배송 상태, 주소, 추적 정보 등을 관리
 *
 * @author ubuthebear
 * @version 1.0
 * @see Order
 * @see ShippingStatus
 */
@Entity
@Table(name = "shippings")  // 데이터베이스 테이블명을 'shippings'로 지정
@Getter @Setter  // Lombok을 사용하여 getter/setter 메서드 자동 생성
@NoArgsConstructor  // 파라미터가 없는 기본 생성자를 생성 (JPA 요구사항)
@EntityListeners(AuditingEntityListener.class)  // JPA Auditing 기능을 사용하여 생성/수정 시간 자동 관리
public class Shipping {
    /**
     * 배송 정보의 고유 식별자
     * 자동 증가(AUTO_INCREMENT) 전략을 사용하는 기본키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shippingId;

    /**
     * 배송과 연관된 주문 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     * 주문(Order)과 일대일(1:1) 관계
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * 운송장 번호
     * 배송 추적에 사용되는 고유 번호
     */
    private String trackingNumber;

    /**
     * 배송업체명
     * 예: "CJ대한통운", "롯데택배" 등
     */
    private String carrier;

    /**
     * 배송 상태
     * PREPARING(배송준비중), SHIPPED(출고완료), DELIVERING(배송중),
     * DELIVERED(배송완료), CANCELLED(배송취소) 등의 상태를 가짐
     */
    @Enumerated(EnumType.STRING)
    private ShippingStatus status = ShippingStatus.PREPARING;  // 초기 상태는 배송준비중

    /**
     * 수령인 정보
     * 배송받을 사람의 이름과 연락처
     */
    private String recipientName;    // 수령인 이름
    private String recipientPhone;   // 수령인 연락처

    /**
     * 배송지 정보
     * 상세 주소와 배송 요청사항
     */
    private String shippingAddress;  // 배송지 주소
    private String shippingMessage;  // 배송 메시지 (예: "부재시 경비실에 맡겨주세요")

    /**
     * 배송 정보 생성 일시
     * 엔티티 생성 시 자동으로 설정되며, 이후 수정 불가
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 배송 정보 수정 일시
     * 엔티티가 수정될 때마다 자동으로 업데이트
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 배송 완료 예정일
     * 배송업체에서 제공하는 예상 배송 완료 시점
     */
    private LocalDateTime estimatedDeliveryDate;

    /**
     * 실제 배송 완료일
     * 실제로 배송이 완료된 시점을 기록
     */
    private LocalDateTime actualDeliveryDate;
}
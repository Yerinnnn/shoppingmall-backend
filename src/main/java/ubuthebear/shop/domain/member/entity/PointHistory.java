package ubuthebear.shop.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ubuthebear.shop.domain.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 포인트 적립/사용 이력을 관리하는 엔티티 클래스
 * 회원의 포인트 변동 내역을 추적하고 기록하는 역할
 * 포인트의 적립, 사용, 만료, 취소 등 모든 변동 사항을 저장
 *
 * @author ubuthebear
 * @version 1.0
 * @see Member
 * @see Order
 * @see PointType
 */
@Entity
@Table(name = "point_histories")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    /**
     * 포인트 변동이 발생한 회원 정보
     * 회원과 N:1 관계로 매핑
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 적립 또는 사용된 포인트 금액
     * BigDecimal을 사용하여 정확한 금액을 표현
     * null이 될 수 없음 (필수 입력 값)
     */
    @Column(nullable = false)
    private BigDecimal amount;

    /**
     * 포인트 변동의 유형
     * EARN(적립), USE(사용), EXPIRE(만료), CANCEL(취소), ADJUST(조정)
     * null이 될 수 없음 (필수 입력 값)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType type;

    /**
     * 포인트 변동에 대한 설명
     * 예: "구매 적립", "주문 사용", "이벤트 적립" 등
     * null이 될 수 없음 (필수 입력 값)
     */
    @Column(nullable = false)
    private String description;

    /**
     * 포인트 변동과 관련된 주문 정보
     * 주문과 연관된 포인트 변동의 경우에만 값이 존재
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * 포인트 변동 후의 잔액
     * 변동 시점의 최종 포인트 잔액을 기록
     * 추후 포인트 정합성 검증에 활용
     */
    private BigDecimal balanceAfter;

    /**
     * 포인트 이력 생성 일시
     * JPA Auditing을 통해 자동으로 설정
     * 한번 저장된 후에는 수정 불가
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
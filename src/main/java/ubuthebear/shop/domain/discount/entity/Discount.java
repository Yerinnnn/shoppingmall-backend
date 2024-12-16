package ubuthebear.shop.domain.discount.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ubuthebear.shop.domain.member.entity.Member;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 할인 정책 정보를 저장하는 엔티티 클래스
 * 상품이나 주문에 적용할 수 있는 할인 정책을 관리
 *
 * @author ubuthebear
 * @version 1.0
 * @see DiscountType
 */
@Entity
@Table(name = "discounts")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Discount {

    /**
     * 할인 정책의 고유 식별자
     * 자동 증가 전략을 사용하는 기본키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountId;

    /**
     * 할인 정책명
     * null이 될 수 없음 (필수 입력 값)
     * 예: "신규 가입자 할인", "여름 맞이 할인" 등
     */
    @Column(nullable = false)
    private String name;

    /**
     * 할인 정책 설명
     * null이 될 수 없음 (필수 입력 값)
     * 할인 정책에 대한 상세 설명을 저장
     */
    @Column(nullable = false)
    private String description;

    /**
     * 할인 유형
     * PERCENTAGE: 퍼센트 할인 (예: 10% 할인)
     * FIXED_AMOUNT: 정액 할인 (예: 5,000원 할인)
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType type;

    /**
     * 할인값
     * type이 PERCENTAGE일 경우: 할인율 (%)
     * type이 FIXED_AMOUNT일 경우: 할인금액 (원)
     */
    @Column(nullable = false)
    private BigDecimal value;  // 할인율(%) 또는 할인금액

    /**
     * 최소 주문금액
     * 할인을 적용하기 위한 최소 주문금액 조건
     * null 가능 (조건이 없는 경우)
     */
    private BigDecimal minimumOrderAmount;  // 최소 주문금액

    /**
     * 최대 할인금액
     * 할인될 수 있는 최대 금액
     * null 가능 (제한이 없는 경우)
     */
    private BigDecimal maximumDiscountAmount;  // 최대 할인금액

    /**
     * 할인 정책 활성화 여부
     * true: 사용 가능한 할인 정책
     * false: 사용 중지된 할인 정책
     */
    @Column(nullable = false)
    private boolean active = true;

    /**
     * 할인 정책 시작 일시
     * null이 될 수 없음 (필수 입력 값)
     */
    @Column(nullable = false)
    private LocalDateTime startAt;

    /**
     * 할인 정책 종료 일시
     * null이 될 수 없음 (필수 입력 값)
     */
    @Column(nullable = false)
    private LocalDateTime endAt;

    /**
     * 할인 정책 생성 일시
     * JPA Auditing을 통해 자동으로 설정
     * 한번 저장된 후에는 수정 불가
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 할인 정책 수정 일시
     * JPA Auditing을 통해 자동으로 업데이트
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
package ubuthebear.shop.domain.discount.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ubuthebear.shop.domain.member.entity.Member;

import java.time.LocalDateTime;


/**
 * 쿠폰 정보를 저장하는 엔티티 클래스
 * 회원에게 발급된 할인 쿠폰의 정보를 관리
 *
 * @author ubuthebear
 * @version 1.0
 * @see Discount
 * @see Member
 */
@Entity
@Table(name = "coupons")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Coupon {

    /**
     * 쿠폰의 고유 식별자
     * 자동 증가 전략을 사용하는 기본키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    /**
     * 쿠폰과 연관된 할인 정책 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     * 할인(Discount)과 다대일(N:1) 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount;

    /**
     * 쿠폰 고유 코드
     * null이 될 수 없으며(필수 입력 값), 중복될 수 없음
     * 쿠폰 식별 및 사용 시 입력값으로 활용
     */
    @Column(nullable = false, unique = true)
    private String code;  // 쿠폰 코드

    /**
     * 쿠폰을 소유한 회원 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     * 회원(Member)과 다대일(N:1) 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // 쿠폰 소유자

    /**
     * 쿠폰의 현재 상태
     * AVAILABLE: 사용 가능
     * USED: 사용됨
     * EXPIRED: 만료됨
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status = CouponStatus.AVAILABLE;

    /**
     * 쿠폰 사용 일시
     * 쿠폰이 사용된 경우에만 값이 설정됨
     */
    private LocalDateTime usedAt;  // 사용 일시

    /**
     * 쿠폰 생성 일시
     * JPA Auditing을 통해 자동으로 설정
     * 한번 저장된 후에는 수정 불가
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 쿠폰 정보 수정 일시
     * JPA Auditing을 통해 자동으로 업데이트
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
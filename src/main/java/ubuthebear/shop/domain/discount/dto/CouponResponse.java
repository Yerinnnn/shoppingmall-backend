package ubuthebear.shop.domain.discount.dto;

import lombok.Data;
import lombok.Getter;
import ubuthebear.shop.domain.discount.entity.Coupon;
import ubuthebear.shop.domain.discount.entity.CouponStatus;

import java.time.LocalDateTime;

/**
 * 쿠폰 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * Coupon 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see Coupon
 * @see CouponStatus
 */
@Getter
public class CouponResponse {
    private final Long couponId;           // 쿠폰의 고유 식별자
    private final String code;             // 쿠폰 코드
    private final DiscountResponse discount; // 연결된 할인 정책 정보
    private final CouponStatus status;      // 쿠폰의 현재 상태
    private final LocalDateTime usedAt;     // 쿠폰 사용 일시
    private final LocalDateTime createdAt;  // 쿠폰 생성 일시

    /**
     * Coupon 엔티티를 CouponResponse DTO로 변환하는 생성자
     *
     * @param coupon 변환할 Coupon 엔티티
     */
    public CouponResponse(Coupon coupon) {
        this.couponId = coupon.getCouponId();
        this.code = coupon.getCode();
        this.discount = new DiscountResponse(coupon.getDiscount());
        this.status = coupon.getStatus();
        this.usedAt = coupon.getUsedAt();
        this.createdAt = coupon.getCreatedAt();
    }
}
package ubuthebear.shop.domain.discount.dto;

import lombok.Getter;
import java.math.BigDecimal;

/**
 * 쿠폰 적용 결과를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * 쿠폰 적용 후의 할인 금액과 최종 가격 정보를 포함
 *
 * @author ubuthebear
 * @version 1.0
 */
@Getter
public class CouponApplicationResponse {
    private final Long couponId;           // 적용된 쿠폰의 고유 식별자
    private final BigDecimal discountAmount; // 할인된 금액
    private final BigDecimal finalPrice;     // 할인 적용 후 최종 가격

    public CouponApplicationResponse(Long couponId, BigDecimal discountAmount, BigDecimal finalPrice) {
        this.couponId = couponId;
        this.discountAmount = discountAmount;
        this.finalPrice = finalPrice;
    }
}
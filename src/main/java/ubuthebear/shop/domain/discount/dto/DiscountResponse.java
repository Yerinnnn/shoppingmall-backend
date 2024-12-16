package ubuthebear.shop.domain.discount.dto;

import lombok.*;
import ubuthebear.shop.domain.discount.entity.Discount;
import ubuthebear.shop.domain.discount.entity.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 할인 정책 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * Discount 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see Discount
 * @see DiscountType
 */
@Getter
public class DiscountResponse {
    private final Long discountId;          // 할인 정책의 고유 식별자
    private final String name;              // 할인 정책명
    private final String description;       // 할인 정책 설명
    private final DiscountType type;        // 할인 유형 (정액/정률)
    private final BigDecimal value;         // 할인값 (금액 또는 비율)
    private final BigDecimal minimumOrderAmount;  // 최소 주문 금액
    private final BigDecimal maximumDiscountAmount; // 최대 할인 금액
    private final boolean active;           // 할인 정책 활성화 여부
    private final LocalDateTime startAt;    // 할인 시작 일시
    private final LocalDateTime endAt;      // 할인 종료 일시
    private final LocalDateTime createdAt;  // 할인 정책 생성 일시

    /**
     * Discount 엔티티를 DiscountResponse DTO로 변환하는 생성자
     *
     * @param discount 변환할 Discount 엔티티
     */
    public DiscountResponse(Discount discount) {
        this.discountId = discount.getDiscountId();
        this.name = discount.getName();
        this.description = discount.getDescription();
        this.type = discount.getType();
        this.value = discount.getValue();
        this.minimumOrderAmount = discount.getMinimumOrderAmount();
        this.maximumDiscountAmount = discount.getMaximumDiscountAmount();
        this.active = discount.isActive();
        this.startAt = discount.getStartAt();
        this.endAt = discount.getEndAt();
        this.createdAt = discount.getCreatedAt();
    }
}
package ubuthebear.shop.domain.discount.dto;

import lombok.Data;
import ubuthebear.shop.domain.discount.entity.DiscountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 할인 정책 생성 요청을 위한 DTO (Data Transfer Object) 클래스
 * 클라이언트로부터 할인 정책 생성에 필요한 정보를 전달받음
 *
 * @author ubuthebear
 * @version 1.0
 * @see DiscountType
 */
@Data
public class DiscountRequest {
    private String name;                    // 할인 정책명
    private String description;             // 할인 정책 설명
    private DiscountType type;              // 할인 유형 (정액/정률)
    private BigDecimal value;               // 할인값 (금액 또는 비율)
    private BigDecimal minimumOrderAmount;  // 최소 주문 금액
    private BigDecimal maximumDiscountAmount; // 최대 할인 금액
    private LocalDateTime startAt;          // 할인 시작 일시
    private LocalDateTime endAt;            // 할인 종료 일시
}
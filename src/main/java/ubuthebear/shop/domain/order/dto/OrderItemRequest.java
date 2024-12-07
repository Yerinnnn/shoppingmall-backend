package ubuthebear.shop.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 주문 상품 정보를 전달받기 위한 요청 DTO (Data Transfer Object) 클래스
 * 클라이언트로부터 주문할 상품의 정보를 전달받는데 사용
 *
 * @author ubuthebear
 * @version 1.0
 */
@Getter @Setter
public class OrderItemRequest {
    /**
     * 주문할 상품의 ID
     * null이 될 수 없음 (필수 입력 값)
     */
    @NotNull(message = "상품 ID는 필수입니다")
    private Long productId;

    /**
     * 주문할 상품의 수량
     * null이 될 수 없음 (필수 입력 값)
     */
    @NotNull(message = "수량은 필수입니다")
    private Integer quantity;
}
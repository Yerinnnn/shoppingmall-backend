package ubuthebear.shop.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 장바구니 상품 추가/수정을 위한 요청 DTO (Data Transfer Object) 클래스
 * 클라이언트로부터 장바구니 상품 정보를 전달받는데 사용
 *
 * @author ubuthebear
 * @version 1.0
 */
@Getter @Setter
public class CartRequest {

    /**
     * 장바구니에 담을 상품의 ID
     * null이 될 수 없음 (필수 입력 값)
     */
    @NotNull(message = "상품 ID는 필수입니다")
    private Long productId;

    /**
     * 장바구니에 담을 상품의 수량
     * null이 될 수 없으며, 최소 1개 이상이어야 함
     */
    @NotNull(message = "수량은 필수입니다")
    @Min(value = 1, message = "수량은 1개 이상이어야 합니다")
    private Integer quantity;
}
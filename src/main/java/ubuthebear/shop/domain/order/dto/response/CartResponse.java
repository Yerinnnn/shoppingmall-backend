package ubuthebear.shop.domain.order.dto.response;

import lombok.Getter;
import ubuthebear.shop.domain.order.entity.Cart;

import java.math.BigDecimal;

/**
 * 장바구니 상품 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * Cart 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see Cart
 */
@Getter
public class CartResponse {
    private final Long cartId;           // 장바구니 아이템 고유 식별자
    private final Long productId;        // 상품 ID
    private final String productName;    // 상품명
    private final BigDecimal price;      // 상품 단가
    private final Integer quantity;      // 수량
    private final BigDecimal totalPrice; // 총 금액 (단가 * 수량)

    /**
     * Cart 엔티티를 CartResponse DTO로 변환하는 생성자
     * 장바구니에 담긴 상품의 정보를 클라이언트에게 제공하기 적합한 형태로 변환
     *
     * @param cart 변환할 Cart 엔티티
     */
    public CartResponse(Cart cart) {
        this.cartId = cart.getCartId();
        this.productId = cart.getProduct().getProductId();
        this.productName = cart.getProduct().getName();
        this.price = cart.getProduct().getPrice();
        this.quantity = cart.getQuantity();
        this.totalPrice = price.multiply(BigDecimal.valueOf(quantity));  // 총 금액 계산
    }
}
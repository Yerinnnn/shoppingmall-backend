package ubuthebear.shop.domain.order.dto;

import lombok.Getter;
import ubuthebear.shop.domain.order.entity.OrderItem;

import java.math.BigDecimal;

/**
 * 주문 상품 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * OrderItem 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see OrderItem
 */
@Getter
public class OrderItemResponse {
    private final Long orderItemId;     // 주문 상품 고유 식별자
    private final Long productId;       // 상품 ID
    private final String productName;   // 상품명
    private final Integer quantity;     // 주문 수량
    private final BigDecimal price;     // 상품 단가
    private final BigDecimal subtotal;  // 소계 금액 (단가 * 수량)

    /**
     * OrderItem 엔티티를 OrderItemResponse DTO로 변환하는 생성자
     * 주문 상품의 상세 정보를 클라이언트에게 제공하기 적합한 형태로 변환
     *
     * @param orderItem 변환할 OrderItem 엔티티
     */
    public OrderItemResponse(OrderItem orderItem) {
        this.orderItemId = orderItem.getOrderItemId();
        this.productId = orderItem.getProduct().getProductId();
        this.productName = orderItem.getProduct().getName();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
        this.subtotal = orderItem.getSubtotal();
    }
}
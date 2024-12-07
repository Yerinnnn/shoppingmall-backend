package ubuthebear.shop.domain.order.dto;

import lombok.Getter;
import ubuthebear.shop.domain.order.entity.Order;
import ubuthebear.shop.domain.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * Order 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see Order
 * @see OrderStatus
 * @see OrderItemResponse
 */
@Getter
public class OrderResponse {
    private final Long orderId;              // 주문 고유 식별자
    private final String orderNumber;        // 주문 번호 (날짜 + ID 조합)
    private final OrderStatus status;        // 주문 상태
    private final BigDecimal totalAmount;    // 총 주문 금액
    private final List<OrderItemResponse> items;  // 주문 상품 목록
    private final String deliveryAddress;    // 배송 주소
    private final String paymentMethod;      // 결제 수단
    private final LocalDateTime createdAt;   // 주문 생성 일시

    /**
     * Order 엔티티를 OrderResponse DTO로 변환하는 생성자
     * 주문의 모든 정보를 클라이언트에게 제공하기 적합한 형태로 변환
     *
     * @param order 변환할 Order 엔티티
     */
    public OrderResponse(Order order) {
        this.orderId = order.getOrderId();
        this.status = order.getStatus();
        this.totalAmount = order.getTotalAmount();
        this.items = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
        this.deliveryAddress = order.getDeliveryAddress().getAddress();
        this.paymentMethod = order.getPaymentMethod().getPaymentType();
        this.createdAt = order.getCreatedAt();
        // 주문번호 생성 규칙: "ORD" + 날짜(epoch일) + 주문ID
        this.orderNumber = String.format("ORD%d%d",
                order.getCreatedAt().toLocalDate().toEpochDay(), orderId);
    }
}

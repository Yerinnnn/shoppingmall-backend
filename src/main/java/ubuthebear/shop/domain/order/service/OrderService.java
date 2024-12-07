package ubuthebear.shop.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.member.entity.Address;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.entity.PaymentMethod;
import ubuthebear.shop.domain.order.dto.OrderItemRequest;
import ubuthebear.shop.domain.order.dto.OrderRequest;
import ubuthebear.shop.domain.order.dto.OrderResponse;
import ubuthebear.shop.domain.order.entity.*;
import ubuthebear.shop.domain.order.repository.OrderRepository;
import ubuthebear.shop.domain.member.repository.MemberRepository;
import ubuthebear.shop.domain.product.entity.Product;
import ubuthebear.shop.domain.product.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 관리를 위한 서비스 클래스
 * 주문의 생성, 취소, 조회 등 주문과 관련된 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // 기본적으로 읽기 전용 트랜잭션으로 설정
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * 새로운 주문을 생성
     * 재고 확인 및 차감, 총 금액 계산 등의 처리를 포함
     *
     * @param username 주문자의 사용자명
     * @param request 주문 생성 요청 정보
     * @return OrderResponse 생성된 주문 정보
     * @throws RuntimeException 회원을 찾을 수 없거나, 배송지/결제수단이 잘못되었거나, 재고가 부족한 경우
     */
    @Transactional
    public OrderResponse createOrder(String username, OrderRequest request) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Order order = new Order();
        order.setMember(member);

        // 배송지 설정
        Address deliveryAddress = member.getAddresses().stream()
                .filter(addr -> addr.getAddressId().equals(request.getDeliveryAddressId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Delivery address not found"));
        order.setDeliveryAddress(deliveryAddress);

        // 결제 수단 설정
        PaymentMethod paymentMethod = member.getPaymentMethods().stream()
                .filter(pm -> pm.getPaymentMethodId().equals(request.getPaymentMethodId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Payment method not found"));
        order.setPaymentMethod(paymentMethod);

        // 주문 상품 추가
        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // 재고 확인
            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());
            order.addOrderItem(orderItem);

            // 재고 감소
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
        }

        order.calculateTotalAmount();
        return new OrderResponse(orderRepository.save(order));
    }

    /**
     * 주문을 취소하고 재고를 복구
     * PENDING 또는 PAID 상태의 주문만 취소 가능
     *
     * @param username 주문자의 사용자명
     * @param orderId 취소할 주문 ID
     * @return OrderResponse 취소된 주문 정보
     * @throws RuntimeException 주문을 찾을 수 없거나, 권한이 없거나, 취소 불가능한 상태인 경우
     */
    @Transactional
    public OrderResponse cancelOrder(String username, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PAID) {
            throw new RuntimeException("Order cannot be cancelled");
        }

        // 재고 복구
        order.getOrderItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        });

        order.setStatus(OrderStatus.CANCELLED);
        return new OrderResponse(order);
    }

    /**
     * 사용자의 주문 목록을 조회
     *
     * @param username 조회할 사용자명
     * @return List<OrderResponse> 주문 목록
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    public List<OrderResponse> getMyOrders(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return orderRepository.findByMember(member).stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 주문의 상세 정보를 조회
     * 본인의 주문만 조회 가능
     *
     * @param username 사용자명
     * @param orderId 조회할 주문 ID
     * @return OrderResponse 주문 상세 정보
     * @throws RuntimeException 주문을 찾을 수 없거나 권한이 없는 경우
     */
    public OrderResponse getOrder(String username, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        return new OrderResponse(order);
    }
}
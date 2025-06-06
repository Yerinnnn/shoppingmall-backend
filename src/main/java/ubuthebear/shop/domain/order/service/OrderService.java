package ubuthebear.shop.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.member.dto.PointBalanceResponse;
import ubuthebear.shop.domain.member.entity.Address;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.entity.PaymentMethod;
import ubuthebear.shop.domain.member.service.LoyaltyPointService;
import ubuthebear.shop.domain.order.dto.OrderItemRequest;
import ubuthebear.shop.domain.order.dto.OrderRequest;
import ubuthebear.shop.domain.order.dto.OrderResponse;
import ubuthebear.shop.domain.order.entity.*;
import ubuthebear.shop.domain.order.repository.OrderRepository;
import ubuthebear.shop.domain.member.repository.MemberRepository;
import ubuthebear.shop.domain.product.entity.Product;
import ubuthebear.shop.domain.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final LoyaltyPointService loyaltyPointService;

    private static final BigDecimal POINT_EARN_RATE = new BigDecimal("0.01"); // 1% 적립

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

        // 포인트 사용 검증
        if (request.getUsePoints().compareTo(BigDecimal.ZERO) > 0) {
            PointBalanceResponse pointBalance = loyaltyPointService.getPointBalance(username);
            if (pointBalance.getBalance().compareTo(request.getUsePoints()) < 0) {
                throw new RuntimeException("Insufficient points");
            }
        }

        Order order = new Order();
        order.setMember(member);
        order.setUsedPoints(request.getUsePoints());

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

        // 주문 상품 추가 및 총액 계산
        addOrderItems(order, request.getItems());
        order.calculateTotalAmount(); // 포인트 차감된 최종 금액 계산

        // 적립 예정 포인트 계산 (주문 금액의 1%)
        BigDecimal earnablePoints = order.getTotalAmount()
                .multiply(POINT_EARN_RATE)
                .setScale(0, RoundingMode.FLOOR); // 소수점 버림
        order.setEarnedPoints(earnablePoints);

        // 주문번호 생성 및 설정
        String orderNumber = generateOrderNumber();
        order.setOrderNumber(orderNumber);

        // 주문 저장
        Order savedOrder = orderRepository.save(order);

        // 포인트 사용 처리
        if (request.getUsePoints().compareTo(BigDecimal.ZERO) > 0) {
            loyaltyPointService.usePoints(
                    username,
                    request.getUsePoints(),
                    "주문 결제 시 포인트 사용",
                    savedOrder
            );
        }

        return new OrderResponse(savedOrder);
    }

    // 주문번호 생성 메서드
    private String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 고유성을 위한 랜덤 또는 시퀀스 번호
        String randomPart = String.format("%06d", (int)(Math.random() * 1000000));
        return "ORD" + datePart + randomPart;
    }

    /**
     * 주문을 취소하고 재고를 복구
     * PENDING 또는 PAID 상태의 주문만 취소 가능
     *
     * @param username 주문자의 사용자명
     * @param orderNumber 취소할 주문 번호
     * @return OrderResponse 취소된 주문 정보
     * @throws RuntimeException 주문을 찾을 수 없거나, 권한이 없거나, 취소 불가능한 상태인 경우
     */
    @Transactional
    public OrderResponse cancelOrder(String username, String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
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

        // 사용한 포인트 환급
        if (order.getUsedPoints().compareTo(BigDecimal.ZERO) > 0) {
            loyaltyPointService.cancelPointUse(
                    username,
                    order.getUsedPoints(),
                    "주문 취소로 인한 포인트 환급",
                    order
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        return new OrderResponse(order);
    }

    /**
     * 주문 구매확정 및 포인트 적립
     * PAID/DELIVERED 상태에서만 가능
     *
     * @param username 주문자 ID
     * @param orderNumber 확정할 주문 번호
     * @return 구매확정된 주문
     * @throws RuntimeException 주문 없음, 권한 없음, 확정 불가 상태
     */
    @Transactional
    public OrderResponse confirmOrder(String username, String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        // PAID, DELIVERED 상태에서 구매확정 가능
        if (order.getStatus() != OrderStatus.PAID &&
                order.getStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("Order cannot be confirmed. Current status: " + order.getStatus());
        }

        // 포인트 적립
        if (order.getEarnedPoints().compareTo(BigDecimal.ZERO) > 0) {
            loyaltyPointService.earnPoints(
                    username,
                    order.getEarnedPoints(),
                    String.format("주문 %s 구매확정 포인트 적립", order.getOrderNumber()),
                    order
            );
        }

        order.setStatus(OrderStatus.COMPLETED);
        return new OrderResponse(orderRepository.save(order));
    }

    /**
     * 주문상품 추가 및 재고 차감
     * 재고 부족시 예외 발생
     */
    private void addOrderItems(Order order, List<OrderItemRequest> items) {
        for (OrderItemRequest itemRequest : items) {
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

            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
        }
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
     * @param orderNumber 조회할 주문 번호
     * @return OrderResponse 주문 상세 정보
     * @throws RuntimeException 주문을 찾을 수 없거나 권한이 없는 경우
     */
    public OrderResponse getOrder(String username, String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        return new OrderResponse(order);
    }
}
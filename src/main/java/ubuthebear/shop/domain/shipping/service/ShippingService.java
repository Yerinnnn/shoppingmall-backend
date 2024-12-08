package ubuthebear.shop.domain.shipping.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.order.entity.Order;
import ubuthebear.shop.domain.order.entity.OrderStatus;
import ubuthebear.shop.domain.order.repository.OrderRepository;
import ubuthebear.shop.domain.shipping.dto.ShippingRequest;
import ubuthebear.shop.domain.shipping.dto.ShippingResponse;
import ubuthebear.shop.domain.shipping.entity.Shipping;
import ubuthebear.shop.domain.shipping.entity.ShippingStatus;
import ubuthebear.shop.domain.shipping.repository.ShippingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 배송 관리를 위한 서비스 클래스
 * 배송 정보의 생성, 수정, 조회 등의 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShippingService {
    private final ShippingRepository shippingRepository;
    private final OrderRepository orderRepository;

    /**
     * 새로운 배송 정보를 생성 (관리자 전용)
     * 주문 정보를 확인하고 배송 정보를 등록
     *
     * @param orderId 배송을 생성할 주문 ID
     * @param request 배송 생성 요청 정보
     * @return ShippingResponse 생성된 배송 정보
     * @throws RuntimeException 주문을 찾을 수 없거나 결제되지 않은 경우
     */
    @Transactional
    public ShippingResponse createShipping(Long orderId, ShippingRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PAID) {
            throw new RuntimeException("Order is not paid");
        }

        Shipping shipping = new Shipping();
        shipping.setOrder(order);
        shipping.setTrackingNumber(request.getTrackingNumber());
        shipping.setCarrier(request.getCarrier());
        shipping.setShippingMessage(request.getShippingMessage());
        shipping.setRecipientName(order.getDeliveryAddress().getMember().getName());
        shipping.setRecipientPhone(order.getDeliveryAddress().getMember().getContact());
        shipping.setShippingAddress(order.getDeliveryAddress().getAddress());
        shipping.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(3));

        order.setStatus(OrderStatus.SHIPPING);

        return new ShippingResponse(shippingRepository.save(shipping));
    }

    /**
     * 배송 상태를 업데이트 (관리자 전용)
     * 배송 완료 시 실제 배송 완료 시간을 기록하고 주문 상태도 함께 업데이트
     *
     * @param shippingId 업데이트할 배송 ID
     * @param newStatus 새로운 배송 상태
     * @return ShippingResponse 업데이트된 배송 정보
     * @throws RuntimeException 배송 정보를 찾을 수 없는 경우
     */
    @Transactional
    public ShippingResponse updateShippingStatus(Long shippingId, ShippingStatus newStatus) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new RuntimeException("Shipping not found"));

        shipping.setStatus(newStatus);

        if (newStatus == ShippingStatus.DELIVERED) {
            shipping.setActualDeliveryDate(LocalDateTime.now());
            shipping.getOrder().setStatus(OrderStatus.DELIVERED);
        }

        return new ShippingResponse(shipping);
    }

    /**
     * 특정 배송 정보를 조회 (일반 사용자용)
     * 본인의 배송 정보만 조회 가능
     *
     * @param username 사용자명
     * @param shippingId 조회할 배송 ID
     * @return ShippingResponse 배송 상세 정보
     * @throws RuntimeException 배송 정보를 찾을 수 없거나 권한이 없는 경우
     */
    public ShippingResponse getShippingDetails(String username, Long shippingId) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new RuntimeException("Shipping not found"));

        if (!shipping.getOrder().getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        return new ShippingResponse(shipping);
    }

    /**
     * 사용자의 모든 배송 목록을 조회 (일반 사용자용)
     *
     * @param username 사용자명
     * @return List<ShippingResponse> 해당 사용자의 전체 배송 목록
     */
    public List<ShippingResponse> getMyShippingList(String username) {
        return shippingRepository.findByOrderMemberUsername(username).stream()
                .map(ShippingResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 운송장 번호로 배송 상태를 추적 (모든 사용자 접근 가능)
     *
     * @param trackingNumber 운송장 번호
     * @return ShippingResponse 배송 정보
     * @throws RuntimeException 배송 정보를 찾을 수 없는 경우
     */
    public ShippingResponse trackShipment(String trackingNumber) {
        Shipping shipping = shippingRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Shipping not found"));
        return new ShippingResponse(shipping);
    }

    /**
     * 전체 배송 목록을 조회 (관리자 전용)
     *
     * @return List<ShippingResponse> 전체 배송 목록
     */
    public List<ShippingResponse> getAllShippings() {
        return shippingRepository.findAll().stream()
                .map(ShippingResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 배송 상태별 목록을 조회 (관리자 전용)
     *
     * @param status 조회할 배송 상태
     * @return List<ShippingResponse> 해당 상태의 배송 목록
     */
    public List<ShippingResponse> getShippingsByStatus(ShippingStatus status) {
        return shippingRepository.findByStatus(status).stream()
                .map(ShippingResponse::new)
                .collect(Collectors.toList());
    }
}
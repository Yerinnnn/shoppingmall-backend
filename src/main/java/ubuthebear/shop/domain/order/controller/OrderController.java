package ubuthebear.shop.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.order.dto.OrderRequest;
import ubuthebear.shop.domain.order.dto.OrderResponse;
import ubuthebear.shop.domain.order.service.OrderService;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 주문 관련 REST API를 제공하는 컨트롤러
 * 주문의 생성, 취소, 조회 등의 엔드포인트를 정의
 *
 * @author ubuthebear
 * @version 1.0
 */
@Tag(name = "Order", description = "주문 관리 API")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 새로운 주문을 생성하는 API 엔드포인트
     * POST /api/orders
     *
     * @param authentication Spring Security 인증 객체
     * @param request 주문 생성 요청 정보
     * @return ResponseEntity<OrderResponse> 생성된 주문 정보
     */
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication,
            @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(authentication.getName(), request));
    }

    @Operation(summary = "주문 취소", description = "주문을 취소합니다.")
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            Authentication authentication,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(authentication.getName(), orderId));
    }

    @Operation(summary = "내 주문 목록 조회", description = "자신의 주문 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getMyOrders(authentication.getName()));
    }

    @Operation(summary = "주문 상세 조회", description = "특정 주문의 상세 정보를 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            Authentication authentication,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(authentication.getName(), orderId));
    }
}

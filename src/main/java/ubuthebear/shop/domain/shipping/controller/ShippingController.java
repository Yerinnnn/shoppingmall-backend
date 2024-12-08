package ubuthebear.shop.domain.shipping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.shipping.dto.ShippingRequest;
import ubuthebear.shop.domain.shipping.dto.ShippingResponse;
import ubuthebear.shop.domain.shipping.entity.ShippingStatus;
import ubuthebear.shop.domain.shipping.service.ShippingService;

import java.util.List;

/**
 * 배송 관련 REST API를 제공하는 컨트롤러
 * 배송 정보의 등록, 수정, 조회 등의 엔드포인트를 정의
 *
 * @author ubuthebear
 * @version 1.0
 */
@Tag(name = "Shipping", description = "배송 관리 API")
@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;

    /**
     * 새로운 배송 정보를 등록하는 API 엔드포인트 (관리자 전용)
     * POST /api/shipping/orders/{orderId}
     *
     * @param orderId 배송을 등록할 주문 ID
     * @param request 배송 등록 요청 정보
     * @return ResponseEntity<ShippingResponse> 등록된 배송 정보
     * @throws RuntimeException 주문을 찾을 수 없거나 결제되지 않은 경우
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[관리자] 배송 정보 등록", description = "새로운 배송 정보를 등록합니다.")
    @PostMapping("/orders/{orderId}")
    public ResponseEntity<ShippingResponse> createShipping(
            @PathVariable Long orderId,
            @Valid @RequestBody ShippingRequest request) {
        return ResponseEntity.ok(shippingService.createShipping(orderId, request));
    }

    /**
     * 배송 상태를 업데이트하는 API 엔드포인트 (관리자 전용)
     * PUT /api/shipping/{shippingId}/status
     *
     * @param shippingId 업데이트할 배송 ID
     * @param status 새로운 배송 상태
     * @return ResponseEntity<ShippingResponse> 업데이트된 배송 정보
     * @throws RuntimeException 배송 정보를 찾을 수 없는 경우
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[관리자] 배송 상태 업데이트", description = "배송 상태를 업데이트합니다.")
    @PutMapping("/{shippingId}/status")
    public ResponseEntity<ShippingResponse> updateShippingStatus(
            @PathVariable Long shippingId,
            @RequestParam ShippingStatus status) {
        return ResponseEntity.ok(shippingService.updateShippingStatus(shippingId, status));
    }

    /**
     * 배송 상세 정보를 조회하는 API 엔드포인트 (일반 사용자용)
     * GET /api/shipping/{shippingId}
     *
     * @param authentication Spring Security 인증 객체
     * @param shippingId 조회할 배송 ID
     * @return ResponseEntity<ShippingResponse> 배송 상세 정보
     * @throws RuntimeException 배송 정보를 찾을 수 없거나 권한이 없는 경우
     */
    @Operation(summary = "배송 상세 조회", description = "배송 상세 정보를 조회합니다.")
    @GetMapping("/{shippingId}")
    public ResponseEntity<ShippingResponse> getShippingDetails(
            Authentication authentication,
            @PathVariable Long shippingId) {
        return ResponseEntity.ok(shippingService.getShippingDetails(
                authentication.getName(), shippingId));
    }

    /**
     * 사용자의 배송 목록을 조회하는 API 엔드포인트 (일반 사용자용)
     * GET /api/shipping/my
     *
     * @param authentication Spring Security 인증 객체
     * @return ResponseEntity<List<ShippingResponse>> 사용자의 배송 목록
     */
    @Operation(summary = "내 배송 목록 조회", description = "사용자의 모든 배송 목록을 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<List<ShippingResponse>> getMyShippingList(Authentication authentication) {
        return ResponseEntity.ok(shippingService.getMyShippingList(authentication.getName()));
    }

    /**
     * 운송장 번호로 배송을 추적하는 API 엔드포인트 (모든 사용자 접근 가능)
     * GET /api/shipping/track/{trackingNumber}
     *
     * @param trackingNumber 운송장 번호
     * @return ResponseEntity<ShippingResponse> 배송 추적 정보
     * @throws RuntimeException 배송 정보를 찾을 수 없는 경우
     */
    @Operation(summary = "배송 추적", description = "운송장 번호로 배송 상태를 추적합니다.")
    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<ShippingResponse> trackShipment(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(shippingService.trackShipment(trackingNumber));
    }
}
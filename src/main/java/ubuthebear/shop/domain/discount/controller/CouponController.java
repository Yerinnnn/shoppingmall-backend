package ubuthebear.shop.domain.discount.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.discount.dto.*;
import ubuthebear.shop.domain.discount.service.CouponService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 쿠폰 관련 REST API를 제공하는 컨트롤러
 * 쿠폰의 생성, 조회, 적용 등의 엔드포인트를 정의
 *
 * @author ubuthebear
 * @version 1.0
 */
@Tag(name = "Coupon", description = "쿠폰 관리 API")
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    /**
     * 새로운 쿠폰을 생성하는 API 엔드포인트
     * ADMIN 권한을 가진 사용자만 접근 가능
     * POST /api/coupons
     *
     * @param request 쿠폰 생성에 필요한 정보를 담은 요청 객체
     * @return ResponseEntity<CouponResponse> 생성된 쿠폰 정보를 담은 응답
     * @throws RuntimeException 할인 정책이나 회원을 찾을 수 없는 경우
     */
    @Operation(summary = "쿠폰 생성", description = "새로운 쿠폰을 생성합니다.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CouponResponse> createCoupon(@RequestBody CouponRequest request) {
        return ResponseEntity.ok(couponService.createCoupon(request));
    }

    /**
     * 현재 로그인한 회원의 사용 가능한 쿠폰 목록을 조회하는 API 엔드포인트
     * GET /api/coupons/my
     *
     * @param authentication Spring Security 인증 객체
     * @return ResponseEntity<List<CouponResponse>> 회원이 보유한 사용 가능한 쿠폰 목록
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    @Operation(summary = "내 쿠폰 조회", description = "사용 가능한 쿠폰 목록을 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<List<CouponResponse>> getMyCoupons(Authentication authentication) {
        return ResponseEntity.ok(couponService.getMemberCoupons(authentication.getName()));
    }

    /**
     * 주문에 쿠폰을 적용하여 할인 금액을 계산하는 API 엔드포인트
     * POST /api/coupons/apply
     *
     * @param authentication Spring Security 인증 객체
     * @param couponCode 적용할 쿠폰 코드
     * @param orderAmount 주문 총액
     * @return ResponseEntity<CouponApplicationResponse> 쿠폰 적용 결과(할인 금액, 최종 결제 금액 등)
     * @throws RuntimeException 쿠폰이 유효하지 않거나, 회원의 쿠폰이 아닌 경우, 쿠폰이 만료된 경우
     */
    @Operation(summary = "쿠폰 적용", description = "주문에 쿠폰을 적용하여 할인 금액을 계산합니다.")
    @PostMapping("/apply")
    public ResponseEntity<CouponApplicationResponse> applyCoupon(
            Authentication authentication,
            @RequestParam String couponCode,
            @RequestParam BigDecimal orderAmount) {
        return ResponseEntity.ok(
                couponService.applyCoupon(authentication.getName(), couponCode, orderAmount)
        );
    }

    /**
     * 쿠폰을 사용 완료 상태로 변경하는 API 엔드포인트
     * POST /api/coupons/{couponId}/use
     *
     * @param couponId 사용 처리할 쿠폰의 ID
     * @return ResponseEntity<Void> 처리 결과
     * @throws RuntimeException 쿠폰을 찾을 수 없는 경우
     */
    @Operation(summary = "쿠폰 사용 처리", description = "쿠폰을 사용 완료 상태로 변경합니다.")
    @PostMapping("/{couponId}/use")
    public ResponseEntity<Void> useCoupon(@PathVariable Long couponId) {
        couponService.markCouponAsUsed(couponId);
        return ResponseEntity.ok().build();
    }

    /**
     * 쿠폰 코드의 유효성을 검증하는 API 엔드포인트
     * 실제로 쿠폰을 적용해보고 가능 여부를 반환
     * GET /api/coupons/validate/{code}
     *
     * @param authentication Spring Security 인증 객체
     * @param code 검증할 쿠폰 코드
     * @return ResponseEntity<Boolean> 쿠폰 사용 가능 여부
     */
    @Operation(summary = "쿠폰 검증", description = "쿠폰 코드의 유효성을 검증합니다.")
    @GetMapping("/validate/{code}")
    public ResponseEntity<Boolean> validateCoupon(
            Authentication authentication,
            @PathVariable String code) {
        try {
            couponService.applyCoupon(authentication.getName(), code, BigDecimal.ONE);
            return ResponseEntity.ok(true);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(false);
        }
    }
}
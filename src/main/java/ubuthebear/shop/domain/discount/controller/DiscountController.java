package ubuthebear.shop.domain.discount.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.discount.dto.*;
import ubuthebear.shop.domain.discount.service.DiscountService;

import java.util.List;

/**
 * 할인 정책 관련 REST API를 제공하는 컨트롤러
 * 할인 정책의 생성, 조회, 활성화 관리 등의 엔드포인트를 정의
 *
 * @author ubuthebear
 * @version 1.0
 */
@Tag(name = "Discount", description = "할인 관리 API")
@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    /**
     * 새로운 할인 정책을 생성하는 API 엔드포인트
     * ADMIN 권한을 가진 사용자만 접근 가능
     * POST /api/discounts
     *
     * @param request 할인 정책 생성에 필요한 정보를 담은 요청 객체
     * @return ResponseEntity<DiscountResponse> 생성된 할인 정책 정보를 담은 응답
     */
    @Operation(summary = "할인 생성", description = "새로운 할인을 생성합니다.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DiscountResponse> createDiscount(@RequestBody DiscountRequest request) {
        return ResponseEntity.ok(discountService.createDiscount(request));
    }

    /**
     * 현재 활성화된 할인 정책 목록을 조회하는 API 엔드포인트
     * GET /api/discounts/active
     *
     * @return ResponseEntity<List<DiscountResponse>> 활성화된 할인 정책 목록
     */
    @Operation(summary = "활성 할인 조회", description = "현재 활성화된 할인 목록을 조회합니다.")
    @GetMapping("/active")
    public ResponseEntity<List<DiscountResponse>> getActiveDiscounts() {
        return ResponseEntity.ok(discountService.getActiveDiscounts());
    }

    /**
     * 할인 정책을 비활성화하는 API 엔드포인트
     * ADMIN 권한을 가진 사용자만 접근 가능
     * POST /api/discounts/{discountId}/deactivate
     *
     * @param discountId 비활성화할 할인 정책의 ID
     * @return ResponseEntity<Void> 처리 결과
     * @throws RuntimeException 할인 정책을 찾을 수 없는 경우
     */
    @Operation(summary = "할인 비활성화", description = "할인을 비활성화합니다.")
    @PostMapping("/{discountId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateDiscount(@PathVariable Long discountId) {
        discountService.deactivateDiscount(discountId);
        return ResponseEntity.ok().build();
    }
}
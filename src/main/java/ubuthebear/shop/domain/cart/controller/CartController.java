package ubuthebear.shop.domain.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.cart.dto.CartRequest;
import ubuthebear.shop.domain.cart.dto.CartResponse;
import ubuthebear.shop.domain.cart.service.CartService;

import java.util.List;

/**
 * 장바구니 관련 REST API를 제공하는 컨트롤러 클래스
 * 장바구니에 대한 CRUD 작업을 처리하는 엔드포인트들을 정의
 *
 * @author ubuthebear
 * @version 1.0
 * @see CartService
 * @see CartRequest
 * @see CartResponse
 */
@Tag(name = "Cart", description = "장바구니 관리 API")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     * 장바구니에 상품을 추가하는 API 엔드포인트
     * POST /api/cart
     *
     * @param authentication Spring Security 인증 객체
     * @param request 장바구니 추가 요청 정보 (상품 ID, 수량 등)
     * @return ResponseEntity<CartResponse> 추가된 장바구니 항목 정보
     * @throws RuntimeException 회원 또는 상품을 찾을 수 없는 경우
     */
    @Operation(summary = "장바구니 추가", description = "상품을 장바구니에 추가합니다.")
    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
            Authentication authentication,
            @RequestBody CartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(authentication.getName(), request));
    }

    /**
     * 장바구니 목록을 조회하는 API 엔드포인트
     * GET /api/cart
     *
     * @param authentication Spring Security 인증 객체
     * @return ResponseEntity<List<CartResponse>> 장바구니 항목 목록
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    @Operation(summary = "장바구니 조회", description = "장바구니 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CartResponse>> getCartItems(Authentication authentication) {
        return ResponseEntity.ok(cartService.getCartItems(authentication.getName()));
    }

    /**
     * 장바구니 항목의 수량을 수정하는 API 엔드포인트
     * PUT /api/cart/{cartId}
     *
     * @param authentication Spring Security 인증 객체
     * @param cartId 수정할 장바구니 항목 ID
     * @param quantity 변경할 수량
     * @return ResponseEntity<Void> 수정 완료 응답
     * @throws RuntimeException 장바구니 항목을 찾을 수 없거나 권한이 없는 경우
     */
    @Operation(summary = "장바구니 수량 수정", description = "장바구니 상품의 수량을 수정합니다.")
    @PutMapping("/{cartId}")
    public ResponseEntity<Void> updateCartItemQuantity(
            // Authentication : Spring Security에서 제공하는 현재 인증된 사용자의 정보를 담고 있는 객체
            Authentication authentication,
            // @PathVariable: URL 경로의 일부를 변수로 사용
            // @RequestParam: URL의 쿼리 파라미터를 변수로 사용
            @PathVariable Long cartId,
            @RequestParam int quantity) {
        cartService.updateCartItemQuantity(authentication.getName(), cartId, quantity);
        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니에서 특정 항목을 삭제하는 API 엔드포인트
     * DELETE /api/cart/{cartId}
     *
     * @param authentication Spring Security 인증 객체
     * @param cartId 삭제할 장바구니 항목 ID
     * @return ResponseEntity<Void> 삭제 완료 응답
     * @throws RuntimeException 장바구니 항목을 찾을 수 없거나 권한이 없는 경우
     */
    @Operation(summary = "장바구니 상품 삭제", description = "장바구니에서 특정 상품을 삭제합니다.")
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeFromCart(
            Authentication authentication,
            @PathVariable Long cartId) {
        cartService.removeFromCart(authentication.getName(), cartId);
        return ResponseEntity.ok().build();
    }

    /**
     * 장바구니를 비우는 API 엔드포인트
     * DELETE /api/cart
     *
     * @param authentication Spring Security 인증 객체
     * @return ResponseEntity<Void> 삭제 완료 응답
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    @Operation(summary = "장바구니 비우기", description = "장바구니를 비웁니다.")
    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ResponseEntity.ok().build();
    }
}
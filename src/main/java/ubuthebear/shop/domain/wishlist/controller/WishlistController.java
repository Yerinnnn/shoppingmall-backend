package ubuthebear.shop.domain.wishlist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.wishlist.dto.WishlistResponse;
import ubuthebear.shop.domain.wishlist.service.WishlistService;

import java.util.List;

/**
 * 위시리스트(찜 목록) 관련 REST API를 제공하는 컨트롤러 클래스
 * 위시리스트에 대한 CRUD 작업을 처리하는 엔드포인트들을 정의
 *
 * @author ubuthebear
 * @version 1.0
 * @see WishlistService
 * @see WishlistResponse
 */
@Tag(name = "Wishlist", description = "위시리스트 관리 API")
@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    /**
     * 위시리스트에 상품을 추가하는 API 엔드포인트
     * POST /api/wishlist/{productId}
     *
     * @param authentication Spring Security 인증 객체
     * @param productId 추가할 상품의 ID
     * @return ResponseEntity<WishlistResponse> 추가된 위시리스트 항목 정보
     * @throws RuntimeException 회원이나 상품을 찾을 수 없거나, 이미 위시리스트에 존재하는 경우
     */
    @Operation(summary = "위시리스트 추가", description = "상품을 위시리스트에 추가합니다.")
    @PostMapping("/{productId}")
    public ResponseEntity<WishlistResponse> addToWishlist(
            Authentication authentication,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(authentication.getName(), productId));
    }

    /**
     * 위시리스트 목록을 조회하는 API 엔드포인트
     * GET /api/wishlist
     *
     * @param authentication Spring Security 인증 객체
     * @return ResponseEntity<List<WishlistResponse>> 위시리스트 항목 목록
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    @Operation(summary = "위시리스트 조회", description = "위시리스트 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishlist(Authentication authentication) {
        return ResponseEntity.ok(wishlistService.getWishlist(authentication.getName()));
    }

    /**
     * 위시리스트에서 특정 항목을 삭제하는 API 엔드포인트
     * DELETE /api/wishlist/{wishlistId}
     *
     * @param authentication Spring Security 인증 객체
     * @param wishlistId 삭제할 위시리스트 항목 ID
     * @return ResponseEntity<Void> 삭제 완료 응답
     * @throws RuntimeException 위시리스트 항목을 찾을 수 없거나 권한이 없는 경우
     */
    @Operation(summary = "위시리스트 삭제", description = "위시리스트에서 특정 상품을 삭제합니다.")
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<Void> removeFromWishlist(
            Authentication authentication,
            @PathVariable Long wishlistId) {
        wishlistService.removeFromWishlist(authentication.getName(), wishlistId);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 상품이 위시리스트에 포함되어 있는지 확인하는 API 엔드포인트
     * GET /api/wishlist/{productId}/exists
     *
     * @param authentication Spring Security 인증 객체
     * @param productId 확인할 상품의 ID
     * @return ResponseEntity<Boolean> 위시리스트 포함 여부
     * @throws RuntimeException 회원이나 상품을 찾을 수 없는 경우
     */
    @Operation(summary = "위시리스트 포함 여부 확인", description = "특정 상품이 위시리스트에 포함되어 있는지 확인합니다.")
    @GetMapping("/{productId}/exists")
    public ResponseEntity<Boolean> isInWishlist(
            Authentication authentication,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.isInWishlist(authentication.getName(), productId));
    }
}
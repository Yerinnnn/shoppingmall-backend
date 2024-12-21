package ubuthebear.shop.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.review.dto.ReviewRequest;
import ubuthebear.shop.domain.review.dto.ReviewResponse;
import ubuthebear.shop.domain.review.service.ReviewService;

/**
 * 리뷰 관련 REST API를 제공하는 컨트롤러
 * 리뷰의 생성, 조회, 수정, 삭제 등의 엔드포인트를 정의
 *
 * @author ubuthebear
 * @version 1.0
 */
@Tag(name = "Review", description = "리뷰 관리 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 새로운 리뷰를 작성하는 API 엔드포인트
     * POST /api/reviews
     *
     * @param authentication Spring Security 인증 객체
     * @param request 리뷰 작성 요청 정보
     * @return ResponseEntity<ReviewResponse> 작성된 리뷰 정보
     * @throws RuntimeException 회원/상품을 찾을 수 없거나, 이미 리뷰가 존재하거나, 구매자가 아닌 경우
     */
    @Operation(summary = "리뷰 작성", description = "상품에 대한 리뷰를 작성합니다.")
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            Authentication authentication,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(
                reviewService.createReview(authentication.getName(), request)
        );
    }

    /**
     * 특정 상품의 리뷰 목록을 조회하는 API 엔드포인트
     * GET /api/reviews/products/{productId}
     *
     * @param productId 조회할 상품 ID
     * @param pageable 페이징 정보
     * @return ResponseEntity<Page<ReviewResponse>> 페이징된 리뷰 목록
     * @throws RuntimeException 상품을 찾을 수 없는 경우
     */
    @Operation(summary = "상품 리뷰 조회", description = "특정 상품의 리뷰 목록을 조회합니다.")
    @GetMapping("/products/{productId}")
    public ResponseEntity<Page<ReviewResponse>> getProductReviews(
            @PathVariable Long productId,
            Pageable pageable) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId, pageable));
    }

    /**
     * 리뷰를 수정하는 API 엔드포인트
     * PUT /api/reviews/{reviewId}
     *
     * @param authentication Spring Security 인증 객체
     * @param reviewId 수정할 리뷰 ID
     * @param request 수정할 리뷰 정보
     * @return ResponseEntity<ReviewResponse> 수정된 리뷰 정보
     * @throws RuntimeException 리뷰를 찾을 수 없거나 권한이 없는 경우
     */
    @Operation(summary = "리뷰 수정", description = "작성한 리뷰를 수정합니다.")
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            Authentication authentication,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(
                reviewService.updateReview(authentication.getName(), reviewId, request)
        );
    }

    /**
     * 리뷰를 삭제하는 API 엔드포인트
     * DELETE /api/reviews/{reviewId}
     *
     * @param authentication Spring Security 인증 객체
     * @param reviewId 삭제할 리뷰 ID
     * @return ResponseEntity<Void> 삭제 완료 응답
     * @throws RuntimeException 리뷰를 찾을 수 없거나 권한이 없는 경우
     */
    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            Authentication authentication,
            @PathVariable Long reviewId) {
        reviewService.deleteReview(authentication.getName(), reviewId);
        return ResponseEntity.ok().build();
    }

    /**
     * 리뷰에 '도움이 됐어요'를 표시하는 API 엔드포인트
     * POST /api/reviews/{reviewId}/helpful
     *
     * @param reviewId 대상 리뷰 ID
     * @return ResponseEntity<Void> 처리 완료 응답
     * @throws RuntimeException 리뷰를 찾을 수 없는 경우
     */
    @Operation(summary = "리뷰 도움됨 표시", description = "리뷰에 '도움이 됐어요'를 표시합니다.")
    @PostMapping("/{reviewId}/helpful")
    public ResponseEntity<Void> markReviewAsHelpful(@PathVariable Long reviewId) {
        reviewService.markReviewAsHelpful(reviewId);
        return ResponseEntity.ok().build();
    }

    /**
     * 상품의 평균 평점을 조회하는 API 엔드포인트
     * GET /api/reviews/products/{productId}/rating
     *
     * @param productId 조회할 상품 ID
     * @return ResponseEntity<Double> 평균 평점
     * @throws RuntimeException 상품을 찾을 수 없는 경우
     */
    @Operation(summary = "상품 평균 평점 조회", description = "특정 상품의 평균 평점을 조회합니다.")
    @GetMapping("/products/{productId}/rating")
    public ResponseEntity<Double> getProductAverageRating(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getProductAverageRating(productId));
    }
}
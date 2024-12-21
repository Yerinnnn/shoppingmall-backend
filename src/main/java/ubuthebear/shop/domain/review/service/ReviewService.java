package ubuthebear.shop.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.repository.MemberRepository;
import ubuthebear.shop.domain.order.entity.OrderItem;
import ubuthebear.shop.domain.order.repository.OrderItemRepository;
import ubuthebear.shop.domain.product.entity.Product;
import ubuthebear.shop.domain.product.repository.ProductRepository;
import ubuthebear.shop.domain.review.dto.ReviewRequest;
import ubuthebear.shop.domain.review.dto.ReviewResponse;
import ubuthebear.shop.domain.review.entity.Review;
import ubuthebear.shop.domain.review.entity.ReviewStatus;
import ubuthebear.shop.domain.review.repository.ReviewRepository;

import java.time.LocalDateTime;

/**
 * 리뷰 관리를 위한 서비스 클래스
 * 리뷰의 생성, 조회, 수정, 삭제 등의 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // 기본적으로 읽기 전용 트랜잭션으로 설정
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 새로운 리뷰를 작성
     * 구매 여부 확인 및 중복 리뷰 검사를 수행
     *
     * @param username 리뷰 작성자의 사용자명
     * @param request 리뷰 작성 요청 정보
     * @return ReviewResponse 작성된 리뷰 정보
     * @throws RuntimeException 회원/상품을 찾을 수 없거나, 이미 리뷰가 존재하거나, 구매자가 아닌 경우
     */
    @Transactional
    public ReviewResponse createReview(String username, ReviewRequest request) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 이미 리뷰를 작성했는지 확인
        if (reviewRepository.existsByOrderItemIdAndStatus(
                request.getOrderItemId(), ReviewStatus.APPROVED)) {
            throw new RuntimeException("Review already exists for this order");
        }

        OrderItem orderItem = orderItemRepository.findById(request.getOrderItemId())
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        // 실제 구매자인지 확인
        if (!orderItem.getOrder().getMember().equals(member)) {
            throw new RuntimeException("Unauthorized access");
        }

        Review review = new Review();
        review.setMember(member);
        review.setProduct(product);
        review.setOrderItem(orderItem);
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setImageUrls(request.getImageUrls());
        review.setStatus(ReviewStatus.APPROVED);  // 자동 승인 (필요시 PENDING으로 변경)

        return new ReviewResponse(reviewRepository.save(review));
    }

    /**
     * 특정 상품의 리뷰 목록을 페이징하여 조회
     * 승인된 리뷰만 조회 가능
     *
     * @param productId 조회할 상품 ID
     * @param pageable 페이징 정보
     * @return Page<ReviewResponse> 페이징된 리뷰 목록
     * @throws RuntimeException 상품을 찾을 수 없는 경우
     */
    public Page<ReviewResponse> getProductReviews(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return reviewRepository
                .findByProductAndStatusOrderByCreatedAtDesc(
                        product, ReviewStatus.APPROVED, pageable)
                .map(ReviewResponse::new);
    }

    /**
     * 리뷰를 수정
     * 본인이 작성한 리뷰만 수정 가능
     *
     * @param username 수정 요청자의 사용자명
     * @param reviewId 수정할 리뷰 ID
     * @param request 수정할 리뷰 정보
     * @return ReviewResponse 수정된 리뷰 정보
     * @throws RuntimeException 리뷰를 찾을 수 없거나 권한이 없는 경우
     */
    @Transactional
    public ReviewResponse updateReview(String username, Long reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setImageUrls(request.getImageUrls());

        return new ReviewResponse(review);
    }

    /**
     * 리뷰를 삭제(논리적 삭제)
     * 본인이 작성한 리뷰만 삭제 가능
     *
     * @param username 삭제 요청자의 사용자명
     * @param reviewId 삭제할 리뷰 ID
     * @throws RuntimeException 리뷰를 찾을 수 없거나 권한이 없는 경우
     */
    @Transactional
    public void deleteReview(String username, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        review.setStatus(ReviewStatus.DELETED);
        review.setDeletedAt(LocalDateTime.now());
    }

    /**
     * 리뷰에 '도움이 됐어요' 표시를 추가
     * 리뷰의 도움됨 카운트를 1 증가
     *
     * @param reviewId 대상 리뷰 ID
     * @throws RuntimeException 리뷰를 찾을 수 없는 경우
     */
    @Transactional
    public void markReviewAsHelpful(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setHelpfulCount(review.getHelpfulCount() + 1);
    }

    /**
     * 상품의 평균 평점을 조회
     * 승인된 리뷰만을 대상으로 계산
     *
     * @param productId 조회할 상품 ID
     * @return Double 평균 평점
     * @throws RuntimeException 상품을 찾을 수 없는 경우
     */
    public Double getProductAverageRating(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return reviewRepository.calculateAverageRating(product);
    }
}
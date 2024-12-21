package ubuthebear.shop.domain.review.dto;

import lombok.Getter;
import ubuthebear.shop.domain.review.entity.Review;
import ubuthebear.shop.domain.review.entity.ReviewStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 리뷰 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * Review 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 */
@Getter
public class ReviewResponse {
    private final Long reviewId;           // 리뷰 고유 식별자
    private final String memberName;       // 작성자 이름
    private final String productName;      // 상품명
    private final Integer rating;          // 평점
    private final String content;          // 리뷰 내용
    private final List<String> imageUrls;  // 이미지 URL 목록
    private final ReviewStatus status;     // 리뷰 상태
    private final Integer helpfulCount;    // '도움이 됐어요' 수
    private final LocalDateTime createdAt; // 작성일시

    /**
     * Review 엔티티를 ReviewResponse DTO로 변환하는 생성자
     *
     * @param review 변환할 Review 엔티티
     */
    public ReviewResponse(Review review) {
        this.reviewId = review.getReviewId();
        this.memberName = review.getMember().getName();
        this.productName = review.getProduct().getName();
        this.rating = review.getRating();
        this.content = review.getContent();
        this.imageUrls = review.getImageUrls();
        this.status = review.getStatus();
        this.helpfulCount = review.getHelpfulCount();
        this.createdAt = review.getCreatedAt();
    }
}
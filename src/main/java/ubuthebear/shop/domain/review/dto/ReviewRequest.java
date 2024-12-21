package ubuthebear.shop.domain.review.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

/**
 * 리뷰 작성/수정 요청을 위한 DTO (Data Transfer Object) 클래스
 * 클라이언트로부터 리뷰 작성에 필요한 정보를 전달받음
 *
 * @author ubuthebear
 * @version 1.0
 */
@Data
public class ReviewRequest {
    @NotNull(message = "상품 ID는 필수입니다")
    private Long productId;  // 리뷰 대상 상품 ID

    @NotNull(message = "주문상품 ID는 필수입니다")
    private Long orderItemId;  // 구매 확인을 위한 주문상품 ID

    @NotNull(message = "평점은 필수입니다")
    @Min(value = 1, message = "평점은 1점 이상이어야 합니다")
    @Max(value = 5, message = "평점은 5점 이하여야 합니다")
    private Integer rating;  // 평점 (1-5점)

    @NotBlank(message = "리뷰 내용은 필수입니다")
    @Size(min = 10, max = 1000, message = "리뷰는 10자 이상 1000자 이하여야 합니다")
    private String content;  // 리뷰 내용

    private List<String> imageUrls;  // 리뷰 이미지 URL 목
}
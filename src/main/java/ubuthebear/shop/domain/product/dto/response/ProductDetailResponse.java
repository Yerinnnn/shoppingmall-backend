package ubuthebear.shop.domain.product.dto.response;

import lombok.Getter;
import ubuthebear.shop.domain.product.entity.ProductDetail;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 상품 상세 정보를 반환하기 위한 응답 DTO 클래스
 * ProductDetail 엔티티의 모든 상세 정보를 클라이언트에게 전달
 *
 * @author ubuthebear
 * @version 1.0
 * @see ProductDetail
 */
@Getter
public class ProductDetailResponse {
    private final Long productDetailId;      // 상품 상세 정보 고유 식별자
    private final String content;            // HTML 형식의 상세 설명
    private final List<String> imageUrls;    // 상세 이미지 URL 목록
    private final String manufacturer;        // 제조사
    private final String origin;             // 원산지
    private final String material;           // 소재
    private final String size;               // 크기
    private final String weight;             // 무게
    private final Long viewCount;            // 조회수
    private final LocalDateTime updatedAt;   // 최종 수정일시
    private final LocalDateTime createdAt;   // 최초 등록일시

    /**
     * ProductDetail 엔티티를 ProductDetailResponse DTO로 변환하는 생성자
     *
     * @param detail 변환할 ProductDetail 엔티티
     */
    public ProductDetailResponse(ProductDetail detail) {
        this.productDetailId = detail.getProductDetailId();
        this.content = detail.getContent();
        this.imageUrls = detail.getImageUrls();
        this.manufacturer = detail.getManufacturer();
        this.origin = detail.getOrigin();
        this.material = detail.getMaterial();
        this.size = detail.getSize();
        this.weight = detail.getWeight();
        this.viewCount = detail.getViewCount();
        this.updatedAt = detail.getUpdatedAt();
        this.createdAt = detail.getCreatedAt();
    }
}
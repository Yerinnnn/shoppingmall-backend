package ubuthebear.shop.domain.product.dto.response;

import lombok.Getter;
import ubuthebear.shop.domain.product.entity.Product;
import ubuthebear.shop.domain.product.entity.ProductDetail;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 상품 상세 조회를 위한 응답 DTO 클래스
 * 상품의 모든 정보와 함께 연관된 상세 정보를 포함
 *
 * @author ubuthebear
 * @version 1.0
 * @see Product
 * @see ProductDetail
 */
@Getter
public class ProductResponse {
    // 상품 기본 정보
    private final Long productId;            // 상품 고유 식별자
    private final String name;               // 상품명
    private final BigDecimal price;          // 가격
    private final String description;        // 상품 설명
    private final Integer stockQuantity;      // 재고 수량
    private final Long categoryId;           // 카테고리 ID
    private final String categoryName;       // 카테고리명
    private final LocalDateTime createdAt;   // 등록일시
    private final LocalDateTime updatedAt;   // 수정일시

    // 상품 상세 정보
    private final String manufacturer;       // 제조사
    private final String origin;             // 원산지
    private final String material;           // 소재
    private final String size;               // 크기
    private final String weight;             // 무게

    /**
     * Product 엔티티를 ProductResponse DTO로 변환하는 생성자
     * 상품의 기본 정보와 연관된 상세 정보를 함께 변환
     *
     * @param product 변환할 Product 엔티티
     */
    public ProductResponse(Product product) {
        // 상품 기본 정보 설정
        this.productId = product.getProductId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.stockQuantity = product.getStockQuantity();
        this.categoryId = product.getCategory().getCategoryId();
        this.categoryName = product.getCategory().getName();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();

        // 상품 상세 정보 설정
        ProductDetail detail = product.getProductDetail();
        if (detail != null) {
            this.manufacturer = detail.getManufacturer();
            this.origin = detail.getOrigin();
            this.material = detail.getMaterial();
            this.size = detail.getSize();
            this.weight = detail.getWeight();
        } else {
            // 상세 정보가 없는 경우 null로 설정
            this.manufacturer = null;
            this.origin = null;
            this.material = null;
            this.size = null;
            this.weight = null;
        }
    }
}
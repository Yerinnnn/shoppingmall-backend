package ubuthebear.shop.domain.product.dto.response;

import lombok.Getter;
import ubuthebear.shop.domain.product.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 상품 목록 조회를 위한 간략화된 응답 DTO 클래스
 * 목록 표시에 필요한 핵심 상품 정보만을 포함
 *
 * @author ubuthebear
 * @version 1.0
 * @see Product
 */
@Getter
public class ProductListResponse {
    private final Long productId;           // 상품 고유 식별자
    private final String name;              // 상품명
    private final BigDecimal price;         // 가격
    private final Integer stockQuantity;     // 재고 수량
    private final String categoryName;       // 카테고리명
    private final LocalDateTime createdAt;   // 등록일시

    /**
     * Product 엔티티를 ProductListResponse DTO로 변환하는 생성자
     * 목록 표시에 필요한 필수 정보만 추출
     *
     * @param product 변환할 Product 엔티티
     */
    public ProductListResponse(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.categoryName = product.getCategory().getName();
        this.createdAt = product.getCreatedAt();
    }
}
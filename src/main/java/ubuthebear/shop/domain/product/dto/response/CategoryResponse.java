package ubuthebear.shop.domain.product.dto.response;

import lombok.Getter;
import ubuthebear.shop.domain.product.entity.Category;

/**
 * 카테고리 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * Category 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see Category
 */
@Getter
public class CategoryResponse {
    private final Long categoryId;      // 카테고리 고유 식별자
    private final String name;          // 카테고리명
    private final String description;   // 카테고리 설명
    private final int productCount;     // 카테고리에 속한 상품 수

    /**
     * Category 엔티티를 CategoryResponse DTO로 변환하는 생성자
     *
     * @param category 변환할 Category 엔티티
     */
    public CategoryResponse(Category category) {
        this.categoryId = category.getCategoryId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.productCount = category.getProducts().size();  // 연관된 상품 목록의 크기를 계산
    }
}
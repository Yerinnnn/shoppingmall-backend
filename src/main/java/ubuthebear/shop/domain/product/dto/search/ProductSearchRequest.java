package ubuthebear.shop.domain.product.dto.search;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 상품 검색 조건을 담는 DTO (Data Transfer Object) 클래스
 * 다양한 검색 조건을 조합하여 상품을 검색할 수 있도록 지원
 * 모든 필드는 선택적(optional)으로, null인 경우 해당 조건은 검색에서 제외
 *
 * @author ubuthebear
 * @version 1.0
 */
@Getter @Setter
public class ProductSearchRequest {

    /**
     * 검색 키워드
     * 상품명, 설명 등에서 포함 여부를 검색
     * null인 경우 키워드 검색 조건이 적용되지 않음
     * 예: "노트북", "가방" 등
     */
    private String keyword;

    /**
     * 카테고리 ID
     * 특정 카테고리에 속한 상품만 검색할 때 사용
     * null인 경우 카테고리 제한 없이 검색
     * 예: 1L (전자제품 카테고리), 2L (의류 카테고리) 등
     */
    private Long categoryId;

    /**
     * 최소 가격
     * 설정된 가격 이상의 상품만 검색
     * null인 경우 최소 가격 제한 없이 검색
     * BigDecimal을 사용하여 정확한 금액을 표현
     * 예: new BigDecimal("10000")
     */
    private BigDecimal minPrice;

    /**
     * 최대 가격
     * 설정된 가격 이하의 상품만 검색
     * null인 경우 최대 가격 제한 없이 검색
     * BigDecimal을 사용하여 정확한 금액을 표현
     * 예: new BigDecimal("50000")
     */
    private BigDecimal maxPrice;

    /**
     * 재고 있음 여부
     * true: 재고가 있는 상품만 검색
     * false: 재고가 없는 상품만 검색
     * null: 재고 상태와 관계없이 모든 상품 검색
     */
    private Boolean inStock;
}
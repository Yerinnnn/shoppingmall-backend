package ubuthebear.shop.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 상품 생성 및 수정을 위한 요청 DTO (Data Transfer Object) 클래스
 * 클라이언트로부터 상품 정보를 전달받는데 사용
 * Bean Validation을 통한 입력값 검증을 포함
 *
 * @author ubuthebear
 * @version 1.0
 */
@Getter @Setter
public class ProductRequest {

    /**
     * 상품명
     * 필수 입력 값 (공백이나 null 불가)
     *
     * @NotBlank: 문자열이 null이 아니고, 공백이 아닌 문자를 하나 이상 포함해야 함
     */
    @NotBlank(message = "상품명은 필수입니다")
    private String name;

    /**
     * 상품 가격
     * 필수 입력 값, 양수여야 함
     * BigDecimal을 사용하여 정확한 금액을 표현
     *
     * @NotNull: null 값을 허용하지 않음
     * @Positive: 0보다 큰 양수만 허용
     */
    @NotNull(message = "가격은 필수입니다")
    @Positive(message = "가격은 양수여야 합니다")
    private BigDecimal price;

    /**
     * 상품 설명
     * 선택적 입력 값, 상품에 대한 기본적인 설명을 저장
     */
    private String description;

    /**
     * 재고 수량
     * 필수 입력 값, 0보다 큰 양수만 허용
     *
     * @NotNull: null 값을 허용하지 않음
     * @PositiveOrZero: 0 또는 양수만 허용
     */
    @NotNull(message = "재고 수량은 필수입니다")
    @PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다")
    private Integer stockQuantity;

    /**
     * 카테고리 ID
     * 필수 입력 값, 상품이 속할 카테고리를 지정
     *
     * @NotNull: null 값을 허용하지 않음
     */
    @NotNull(message = "카테고리는 필수입니다")
    private Long categoryId;

    /**
     * ProductDetail 관련 필드들 - 모두 선택적 입력 값
     */
    private String manufacturer;  // 제조사명
    private String origin;        // 원산지 정보
    private String material;      // 소재 정보
    private String size;          // 크기 정보
    private String weight;        // 무게 정보
}
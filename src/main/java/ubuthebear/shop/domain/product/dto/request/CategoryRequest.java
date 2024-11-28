package ubuthebear.shop.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 카테고리 생성 및 수정을 위한 요청 DTO (Data Transfer Object) 클래스
 * 클라이언트로부터 카테고리 정보를 전달받는데 사용
 *
 * @author ubuthebear
 * @version 1.0
 */
@Getter @Setter
public class CategoryRequest {

    /**
     * 카테고리명
     * 필수 입력 값 (공백이나 null 불가)
     *
     * @NotBlank: 문자열이 null이 아니고, 공백이 아닌 문자를 하나 이상 포함해야 함
     */
    @NotBlank(message = "카테고리명은 필수입니다")
    private String name;

    /**
     * 카테고리 설명
     * 선택적 입력 값으로, 카테고리에 대한 부가 설명을 저장
     * null이 허용
     */
    private String description;
}
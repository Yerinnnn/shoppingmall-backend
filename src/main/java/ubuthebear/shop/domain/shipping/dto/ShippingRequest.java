package ubuthebear.shop.domain.shipping.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 배송 정보 등록/수정을 위한 요청 DTO (Data Transfer Object) 클래스
 * 클라이언트로부터 배송 처리에 필요한 정보를 전달받는데 사용
 *
 * @author ubuthebear
 * @version 1.0
 */
@Getter @Setter  // Lombok을 사용하여 getter/setter 메서드 자동 생성
public class ShippingRequest {
    /**
     * 운송장 번호
     * 배송 추적에 사용되는 고유 번호
     * null이나 빈 문자열이 될 수 없음 (필수 입력 값)
     */
    @NotBlank(message = "운송장 번호는 필수입니다")
    private String trackingNumber;

    /**
     * 배송업체명
     * 실제 배송을 수행하는 택배사 정보
     * null이나 빈 문자열이 될 수 없음 (필수 입력 값)
     * 예: "CJ대한통운", "롯데택배" 등
     */
    @NotBlank(message = "배송업체는 필수입니다")
    private String carrier;

    /**
     * 배송 메시지
     * 배송 시 요청사항이나 특이사항
     * 선택적 입력 가능 (null 허용)
     * 예: "부재시 경비실에 맡겨주세요", "배송 전 연락 부탁드립니다" 등
     */
    private String shippingMessage;
}
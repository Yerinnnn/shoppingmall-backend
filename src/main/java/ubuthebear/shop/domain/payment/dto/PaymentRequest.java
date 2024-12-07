package ubuthebear.shop.domain.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 결제 처리를 위한 요청 DTO (Data Transfer Object) 클래스
 * 클라이언트로부터 결제 처리에 필요한 정보를 전달받는데 사용
 *
 * @author ubuthebear
 * @version 1.0
 */
@Getter @Setter
public class PaymentRequest {
    /**
     * 결제할 주문의 ID
     * null이 될 수 없음 (필수 입력 값)
     * 결제 처리할 주문을 식별하는데 사용
     */
    @NotNull(message = "주문 ID는 필수입니다")
    private Long orderId;

    /**
     * PG사에서 발급받은 결제 토큰
     * 실제 결제 처리 시 사용되는 일회성 토큰
     * 보안상의 이유로 결제 시점에만 유효
     * null 허용 (PG사 연동 방식에 따라 선택적으로 사용)
     */
    private String paymentToken;
}
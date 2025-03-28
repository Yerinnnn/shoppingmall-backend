package ubuthebear.shop.domain.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 주문 생성을 위한 요청 DTO (Data Transfer Object) 클래스
 * 클라이언트로부터 새로운 주문 생성에 필요한 모든 정보를 전달받음
 *
 * @author ubuthebear
 * @version 1.0
 * @see OrderItemRequest
 */
@Getter @Setter
public class OrderRequest {
    /**
     * 배송받을 주소의 ID
     * null이 될 수 없음 (필수 입력 값)
     */
    @NotNull(message = "배송지 ID는 필수입니다")
    private Long deliveryAddressId;

    /**
     * 결제에 사용할 결제 수단의 ID
     * null이 될 수 없음 (필수 입력 값)
     */
    @NotNull(message = "결제 수단 ID는 필수입니다")
    private Long paymentMethodId;

    /**
     * 주문할 상품 목록
     * 비어있을 수 없음 (최소 1개 이상의 상품 필요)
     */
    @NotEmpty(message = "주문 상품은 필수입니다")
    private List<OrderItemRequest> items;

    /**
     * 주문 시 사용할 포인트 금액
     * 기본값은 0
     * 회원의 보유 포인트 잔액을 초과할 수 없음
     */
    private BigDecimal usePoints = BigDecimal.ZERO;
}

package ubuthebear.shop.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPrepareResponse {
    private String clientKey;  // 클라이언트 키
    private String orderId;    // 주문 ID
}

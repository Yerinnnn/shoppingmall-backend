package ubuthebear.shop.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmRequest {
    @NotNull(message = "결제 키는 필수입니다")
    private String paymentKey;

    @NotNull(message = "주문 ID는 필수입니다")
    private String orderId;

    @NotNull(message = "결제 금액은 필수입니다")
    private BigDecimal amount;
}

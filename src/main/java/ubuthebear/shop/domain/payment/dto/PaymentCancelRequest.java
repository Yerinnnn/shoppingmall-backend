package ubuthebear.shop.domain.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelRequest {
    @NotBlank(message = "취소 사유는 필수입니다")
    private String cancelReason;
}
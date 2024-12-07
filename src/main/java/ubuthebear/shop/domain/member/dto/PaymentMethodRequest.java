package ubuthebear.shop.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequest {
    private String paymentType;  // 결제수단 유형 (예: CREDIT_CARD)
    private String cardNumber;   // 카드번호
    private String expiryDate;   // 만료일
    private boolean isDefault;   // 기본 결제수단 여부
}

package ubuthebear.shop.domain.member.dto;

import lombok.Getter;
import ubuthebear.shop.domain.member.entity.PaymentMethod;

@Getter
public class PaymentMethodResponse {
    private final Long paymentMethodId;
    private final String paymentType;
    private final String cardNumber;    // 마스킹 처리된 카드번호
    private final String expiryDate;
    private final boolean isDefault;

    public PaymentMethodResponse(PaymentMethod paymentMethod) {
        this.paymentMethodId = paymentMethod.getPaymentMethodId();
        this.paymentType = paymentMethod.getPaymentType();
        this.cardNumber = maskCardNumber(paymentMethod.getCardNumber());
        this.expiryDate = paymentMethod.getExpiryDate();
        this.isDefault = paymentMethod.isDefault();
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return null;
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}

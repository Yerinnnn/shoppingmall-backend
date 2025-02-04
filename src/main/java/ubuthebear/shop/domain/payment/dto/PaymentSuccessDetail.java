package ubuthebear.shop.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSuccessDetail {
    private String paymentMethod;          // 결제 방법 (카드, 가상계좌 등)
    private String cardNumber;             // 마스킹된 카드 번호
    private String cardCompany;            // 카드사 이름
    private Integer installmentPlanMonths; // 할부 개월 수 (일시불은 0)
    private String approveNo;              // 카드사 승인번호
    private String cardType;               // 카드 종류 (신용, 체크 등)
    private String ownerType;              // 카드 소유자 타입 (개인, 법인)
    private String acquireStatus;          // 매입 상태
    private Boolean useCardPoint;          // 카드 포인트 사용 여부
}
package ubuthebear.shop.domain.shipping.dto;

import lombok.Getter;
import ubuthebear.shop.domain.shipping.entity.Shipping;
import ubuthebear.shop.domain.shipping.entity.ShippingStatus;

import java.time.LocalDateTime;

/**
 * 배송 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * Shipping 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see Shipping
 * @see ShippingStatus
 */
@Getter  // Lombok을 사용하여 getter 메서드 자동 생성
public class ShippingResponse {
    private final Long shippingId;              // 배송 정보 고유 식별자
    private final String trackingNumber;        // 운송장 번호
    private final String carrier;               // 배송업체명
    private final ShippingStatus status;        // 배송 상태
    private final String recipientName;         // 수령인 이름
    private final String recipientPhone;        // 수령인 연락처
    private final String shippingAddress;       // 배송지 주소
    private final String shippingMessage;       // 배송 메시지
    private final LocalDateTime estimatedDeliveryDate;  // 예상 배송 완료일
    private final LocalDateTime actualDeliveryDate;     // 실제 배송 완료일
    private final LocalDateTime createdAt;      // 배송 정보 생성일

    /**
     * Shipping 엔티티를 ShippingResponse DTO로 변환하는 생성자
     * 배송 정보를 클라이언트에게 제공하기 적합한 형태로 변환
     *
     * @param shipping 변환할 Shipping 엔티티
     */
    public ShippingResponse(Shipping shipping) {
        this.shippingId = shipping.getShippingId();
        this.trackingNumber = shipping.getTrackingNumber();
        this.carrier = shipping.getCarrier();
        this.status = shipping.getStatus();
        this.recipientName = shipping.getRecipientName();
        this.recipientPhone = shipping.getRecipientPhone();
        this.shippingAddress = shipping.getShippingAddress();
        this.shippingMessage = shipping.getShippingMessage();
        this.estimatedDeliveryDate = shipping.getEstimatedDeliveryDate();
        this.actualDeliveryDate = shipping.getActualDeliveryDate();
        this.createdAt = shipping.getCreatedAt();
    }
}
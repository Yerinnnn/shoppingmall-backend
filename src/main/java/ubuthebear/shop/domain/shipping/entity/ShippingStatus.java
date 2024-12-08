package ubuthebear.shop.domain.shipping.entity;

public enum ShippingStatus {
    PREPARING,    // 배송 준비 중 : 주문은 완료되었으나 아직 출고 전 상태
    SHIPPED,      // 출고 완료 : 상품이 물류센터에서 출고된 상태
    DELIVERING,   // 배송 중 : 배송업체가 실제 배송을 진행 중인 상태
    DELIVERED,    // 배송 완료 : 수령인에게 상품이 전달된 상태
    CANCELLED     // 배송 취소 : 배송이 취소된 상태 (반송, 환불 등의 사유)
}
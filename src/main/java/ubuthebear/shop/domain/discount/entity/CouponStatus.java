package ubuthebear.shop.domain.discount.entity;

/**
 * 쿠폰의 상태를 나타내는 열거형 클래스
 * 쿠폰의 현재 사용 가능 여부를 표현
 */
public enum CouponStatus {
    AVAILABLE,  // 사용 가능
    USED,       // 사용됨
    EXPIRED     // 유효기간 만료됨
}
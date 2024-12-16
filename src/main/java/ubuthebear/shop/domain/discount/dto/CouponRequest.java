package ubuthebear.shop.domain.discount.dto;

import lombok.Data;

/**
 * 쿠폰 생성 요청을 위한 DTO (Data Transfer Object) 클래스
 * 클라이언트로부터 쿠폰 생성에 필요한 정보를 전달받음
 *
 * @author ubuthebear
 * @version 1.0
 */
@Data
public class CouponRequest {
    private Long discountId;    // 연결할 할인 정책의 ID
    private String code;        // 생성할 쿠폰의 코드
    private String memberId;    // 쿠폰을 발급받을 회원의 ID
}
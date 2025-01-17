package ubuthebear.shop.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String postalCode;      // 우편번호
    private String roadAddress;     // 도로명 주소
    private String detailAddress;   // 상세 주소
}

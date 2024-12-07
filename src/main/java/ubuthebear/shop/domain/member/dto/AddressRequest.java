package ubuthebear.shop.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String address;     // 상세 주소
    private String city;        // 도시
    private String postalCode;  // 우편번호
}

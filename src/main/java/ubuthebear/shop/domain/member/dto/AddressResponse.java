package ubuthebear.shop.domain.member.dto;

import lombok.Getter;
import ubuthebear.shop.domain.member.entity.Address;

@Getter
public class AddressResponse {
    private final Long addressId;
    private final String postalCode;
    private final String roadAddress;
    private final String detailAddress;
    private final String fullAddress;

    public AddressResponse(Address address) {
        this.addressId = address.getAddressId();
        this.postalCode = address.getPostalCode();
        this.roadAddress = address.getRoadAddress();
        this.detailAddress = address.getDetailAddress();
        this.fullAddress = address.getFullAddress();
    }
}
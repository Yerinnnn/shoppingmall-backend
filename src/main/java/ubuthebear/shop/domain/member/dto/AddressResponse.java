package ubuthebear.shop.domain.member.dto;

import lombok.Getter;
import ubuthebear.shop.domain.member.entity.Address;

@Getter
public class AddressResponse {
    private final Long addressId;
    private final String address;
    private final String city;
    private final String postalCode;

    public AddressResponse(Address address) {
        this.addressId = address.getAddressId();
        this.address = address.getAddress();
        this.city = address.getCity();
        this.postalCode = address.getPostalCode();
    }
}
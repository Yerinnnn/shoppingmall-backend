package ubuthebear.shop.domain.member.dto;

import lombok.Getter;
import ubuthebear.shop.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberResponse {
    private final Long memberId;
    private final String username;
    private final String name;
    private final String contact;
    private final List<AddressResponse> addresses;
    private final List<PaymentMethodResponse> paymentMethods;
    private final LocalDateTime createdAt;

    public MemberResponse(Member member) {
        this.memberId = member.getMemberId();
        this.username = member.getUsername();
        this.name = member.getName();
        this.contact = member.getContact();
        this.addresses = member.getAddresses().stream()
                .map(AddressResponse::new)
                .collect(Collectors.toList());
        this.paymentMethods = member.getPaymentMethods().stream()
                .map(PaymentMethodResponse::new)
                .collect(Collectors.toList());
        this.createdAt = member.getCreatedAt();
    }
}
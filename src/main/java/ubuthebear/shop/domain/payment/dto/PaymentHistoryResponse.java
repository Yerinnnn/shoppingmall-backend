package ubuthebear.shop.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import ubuthebear.shop.domain.payment.entity.PaymentHistory;
import ubuthebear.shop.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class PaymentHistoryResponse {
    private Long id;
    private PaymentStatus status;
    private String description;
    private LocalDateTime createdAt;

    public PaymentHistoryResponse(PaymentHistory history) {
        this.id = history.getId();
        this.status = history.getStatus();
        this.description = history.getDescription();
        this.createdAt = history.getCreatedAt();
    }
}
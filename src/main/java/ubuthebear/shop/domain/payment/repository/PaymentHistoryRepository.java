package ubuthebear.shop.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.payment.entity.Payment;
import ubuthebear.shop.domain.payment.entity.PaymentHistory;

import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    List<PaymentHistory> findByPaymentOrderByCreatedAtDesc(Payment payment);
}
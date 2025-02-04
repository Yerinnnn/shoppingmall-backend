package ubuthebear.shop.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.payment.entity.Payment;
import ubuthebear.shop.domain.payment.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 결제 정보에 대한 데이터베이스 접근을 담당하는 리포지토리
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 수행
 *
 * @author ubuthebear
 * @version 1.0
 * @see Payment
 * @see Member
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentKey(String paymentKey);
    Optional<Payment> findByOrderId(String orderId);
    List<Payment> findByMemberOrderByCreatedAtDesc(Member member);
    List<Payment> findByStatusAndCreatedAtBefore(PaymentStatus status, LocalDateTime dateTime);
}
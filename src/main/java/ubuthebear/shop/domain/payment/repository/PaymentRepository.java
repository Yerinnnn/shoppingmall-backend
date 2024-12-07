package ubuthebear.shop.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.payment.entity.Payment;

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

    /**
     * 특정 회원의 모든 결제 내역을 조회
     * 주문과 연관된 회원 정보를 통해 결제 내역을 검색
     *
     * @param member 조회할 회원 엔티티
     * @return List<Payment> 해당 회원의 전체 결제 내역
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Payment> findByOrderMember(Member member);

    /**
     * 결제 거래 ID로 결제 정보를 조회
     * PG사에서 제공하는 거래 ID를 통해 결제 정보를 검색
     *
     * @param transactionId PG사에서 제공한 거래 ID
     * @return Optional<Payment> 해당 거래 ID의 결제 정보
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    Optional<Payment> findByTransactionId(String transactionId);
}
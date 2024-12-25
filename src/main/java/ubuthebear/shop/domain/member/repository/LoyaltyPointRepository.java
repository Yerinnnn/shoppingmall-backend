package ubuthebear.shop.domain.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.member.entity.LoyaltyPoint;
import ubuthebear.shop.domain.member.entity.Member;

import java.util.Optional;

/**
 * 포인트 잔액 정보에 대한 데이터베이스 접근을 담당하는 리포지토리
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 수행
 *
 * @author ubuthebear
 * @version 1.0
 * @see LoyaltyPoint
 * @see Member
 */
@Repository
public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, Long> {

    /**
     * 특정 회원의 포인트 정보를 조회
     * 회원당 하나의 포인트 정보만 존재
     *
     * @param member 조회할 회원 엔티티
     * @return Optional<LoyaltyPoint> 회원의 포인트 정보
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    Optional<LoyaltyPoint> findByMember(Member member);

    /**
     * 특정 회원의 포인트 정보를 조회 (잠금 설정)
     * 동시성 제어가 필요한 경우 사용
     * 트랜잭션이 완료될 때까지 해당 레코드에 대한 잠금을 유지
     *
     * @param member 조회할 회원 엔티티
     * @return Optional<LoyaltyPoint> 회원의 포인트 정보
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    @Query("SELECT lp FROM LoyaltyPoint lp WHERE lp.member = :member")
    Optional<LoyaltyPoint> findByMemberWithLock(Member member);
}
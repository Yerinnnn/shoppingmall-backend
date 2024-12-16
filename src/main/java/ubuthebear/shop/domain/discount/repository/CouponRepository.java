package ubuthebear.shop.domain.discount.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.discount.entity.Coupon;
import ubuthebear.shop.domain.discount.entity.CouponStatus;
import ubuthebear.shop.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 쿠폰 데이터 접근을 위한 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 처리하고,
 * 쿠폰 관련 커스텀 쿼리 메서드를 제공
 *
 * @author ubuthebear
 * @version 1.0
 * @see Coupon
 * @see Member
 * @see CouponStatus
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /**
     * 특정 회원의 특정 상태의 쿠폰 목록을 조회
     * 예: 특정 회원의 사용 가능한 쿠폰만 조회
     *
     * @param member 조회할 회원 엔티티
     * @param status 조회할 쿠폰 상태 (AVAILABLE, USED, EXPIRED)
     * @return List<Coupon> 해당 회원의 특정 상태 쿠폰 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Coupon> findByMemberAndStatus(Member member, CouponStatus status);

    /**
     * 특정 쿠폰 코드와 상태로 쿠폰을 조회
     * 쿠폰 사용 시 유효성 검증에 활용
     *
     * @param code 조회할 쿠폰 코드
     * @param status 조회할 쿠폰 상태
     * @return Optional<Coupon> 해당 코드와 상태를 가진 쿠폰 정보
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    Optional<Coupon> findByCodeAndStatus(String code, CouponStatus status);

    /**
     * 특정 회원의 사용 가능한 쿠폰 목록을 조회
     * 현재 시점 기준으로 만료되지 않은 사용 가능한 쿠폰만 조회
     * JPQL을 사용하여 복잡한 조건을 처리
     *
     * @param member 조회할 회원 엔티티
     * @param now 현재 시점
     * @return List<Coupon> 사용 가능한 쿠폰 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    @Query("SELECT c FROM Coupon c WHERE c.member = :member " +
            "AND c.status = 'AVAILABLE' " +
            "AND c.discount.endAt >= :now")
    List<Coupon> findAvailableCoupons(Member member, LocalDateTime now);

    /**
     * 특정 쿠폰 코드의 존재 여부를 확인
     * 쿠폰 생성 시 중복 코드 검사에 활용
     *
     * @param code 확인할 쿠폰 코드
     * @return boolean 쿠폰 코드 존재 여부
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    boolean existsByCode(String code);
}
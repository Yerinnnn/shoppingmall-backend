package ubuthebear.shop.domain.discount.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.discount.entity.Discount;
import ubuthebear.shop.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 할인 정책 데이터 접근을 위한 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 처리하고,
 * 할인 정책 관련 커스텀 쿼리 메서드를 제공
 *
 * @author ubuthebear
 * @version 1.0
 * @see Discount
 * @see Member
 */
@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    /**
     * 활성화된 모든 할인 정책을 조회
     * 기간에 관계없이 active 필드가 true인 할인 정책만 조회
     *
     * @return List<Discount> 활성화된 할인 정책 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Discount> findByActiveTrue();

    /**
     * 현재 적용 가능한 할인 정책을 조회
     * 활성화되어 있고(active = true), 현재 시점이 적용 기간 내인 할인 정책만 조회
     * JPQL을 사용하여 복잡한 조건을 처리
     *
     * @param now 현재 시점
     * @return List<Discount> 현재 적용 가능한 할인 정책 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    @Query("SELECT d FROM Discount d WHERE d.active = true " +
            "AND d.startAt <= :now AND d.endAt >= :now")
    List<Discount> findActiveDiscounts(LocalDateTime now);
}

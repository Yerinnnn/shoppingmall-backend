package ubuthebear.shop.domain.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.order.entity.Order;
import ubuthebear.shop.domain.shipping.entity.Shipping;
import ubuthebear.shop.domain.shipping.entity.ShippingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 배송 정보에 대한 데이터베이스 접근을 담당하는 리포지토리
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 수행
 *
 * @author ubuthebear
 * @version 1.0
 * @see Shipping
 * @see JpaRepository
 */
@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {
    /**
     * 운송장 번호로 배송 정보를 조회
     * 배송 추적 시 사용
     *
     * @param trackingNumber 조회할 운송장 번호
     * @return Optional<Shipping> 해당 운송장 번호의 배송 정보
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    Optional<Shipping> findByTrackingNumber(String trackingNumber);

    /**
     * 특정 배송 상태의 모든 배송 정보를 조회
     * 배송 상태별 목록 조회에 사용
     *
     * @param status 조회할 배송 상태
     * @return List<Shipping> 해당 상태의 모든 배송 정보 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Shipping> findByStatus(ShippingStatus status);

    /**
     * 특정 회원의 모든 배송 정보를 조회
     * 회원별 배송 내역 조회에 사용
     *
     * @param username 조회할 회원의 사용자명
     * @return List<Shipping> 해당 회원의 모든 배송 정보 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Shipping> findByOrderMemberUsername(String username);

    /**
     * 특정 기간 내 예상 배송 완료 예정인 배송 정보를 조회
     * 배송 계획 수립 및 관리에 사용
     *
     * @param start 조회 시작 일시
     * @param end 조회 종료 일시
     * @return List<Shipping> 해당 기간 내 배송 예정인 배송 정보 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Shipping> findByEstimatedDeliveryDateBetween(LocalDateTime start, LocalDateTime end);
}
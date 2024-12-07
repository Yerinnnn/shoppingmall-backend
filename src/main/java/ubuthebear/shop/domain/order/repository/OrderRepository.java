package ubuthebear.shop.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.order.entity.Order;
import ubuthebear.shop.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 정보에 대한 데이터베이스 접근을 담당하는 리포지토리
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 수행
 *
 * @author ubuthebear
 * @version 1.0
 * @see Order
 * @see Member
 * @see OrderStatus
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * 특정 회원의 모든 주문 내역을 조회
     *
     * @param member 조회할 회원 엔티티
     * @return List<Order> 해당 회원의 전체 주문 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Order> findByMember(Member member);

    /**
     * 특정 회원의 특정 상태의 주문 내역을 조회
     * 예: 특정 회원의 '배송 중' 상태인 주문만 조회
     *
     * @param member 조회할 회원 엔티티
     * @param status 조회할 주문 상태
     * @return List<Order> 해당 회원의 특정 상태 주문 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Order> findByMemberAndStatus(Member member, OrderStatus status);

    /**
     * 특정 기간 내의 주문 내역을 조회
     * 시작 시간과 종료 시간 사이에 생성된 주문을 검색
     *
     * @param start 조회 시작 시간
     * @param end 조회 종료 시간
     * @return List<Order> 해당 기간 내의 주문 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    /**
     * 특정 상태의 모든 주문을 조회
     * 예: 모든 '결제 대기 중' 상태의 주문 조회
     *
     * @param status 조회할 주문 상태
     * @return List<Order> 해당 상태의 전체 주문 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Order> findByStatus(OrderStatus status);
}
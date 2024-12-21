package ubuthebear.shop.domain.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.order.entity.OrderItem;
import ubuthebear.shop.domain.product.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 주문 상품 데이터 접근을 위한 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 처리하고,
 * 주문 상품 관련 커스텀 쿼리 메서드를 제공
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * 특정 회원의 주문 상품 목록을 조회
     * 주문과 연관된 회원 정보를 통해 주문 상품을 검색
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.member = :member ORDER BY oi.order.createdAt DESC")
    List<OrderItem> findByMember(Member member);

    /**
     * 특정 상품의 주문 목록을 조회 (페이징)
     * 상품별 판매 이력 확인 등에 사용
     */
    Page<OrderItem> findByProduct(Product product, Pageable pageable);

    /**
     * 특정 기간 동안의 상품별 판매 수량을 집계
     * 매출 통계 등에 활용
     */
    @Query("SELECT oi.product, SUM(oi.quantity) FROM OrderItem oi " +
            "WHERE oi.order.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.product")
    List<Object[]> countSalesByProduct(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 특정 회원이 특정 상품을 구매했는지 확인
     * 리뷰 작성 자격 검증 등에 사용
     */
    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END FROM OrderItem oi " +
            "WHERE oi.order.member = :member AND oi.product = :product")
    boolean existsByMemberAndProduct(Member member, Product product);

    /**
     * 리뷰가 작성되지 않은 주문 상품 목록 조회
     * 리뷰 작성 독려 등에 활용
     */
    @Query("SELECT oi FROM OrderItem oi WHERE NOT EXISTS " +
            "(SELECT r FROM Review r WHERE r.orderItem = oi)")
    List<OrderItem> findOrderItemsWithoutReview();

    /**
     * 특정 상품의 최근 구매자 목록 조회
     * 상품 상세 페이지의 "최근 구매자" 표시 등에 활용
     */
    @Query("SELECT DISTINCT oi.order.member FROM OrderItem oi " +
            "WHERE oi.product = :product " +
            "ORDER BY oi.order.createdAt DESC")
    List<Member> findRecentBuyersByProduct(Product product, Pageable pageable);
}
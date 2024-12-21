// ReviewRepository.java
package ubuthebear.shop.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.review.entity.Review;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.product.entity.Product;
import ubuthebear.shop.domain.review.entity.ReviewStatus;

import java.util.List;

/**
 * 리뷰 데이터 접근을 위한 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 처리하고,
 * 리뷰 관련 커스텀 쿼리 메서드를 제공
 *
 * @author ubuthebear
 * @version 1.0
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    /**
     * 특정 상품의 리뷰를 페이징하여 조회
     * 생성일시 기준 내림차순 정렬
     *
     * @param product 조회할 상품
     * @param status 조회할 리뷰 상태
     * @param pageable 페이징 정보
     * @return Page<Review> 페이징된 리뷰 목록
     */
    Page<Review> findByProductAndStatusOrderByCreatedAtDesc(
            Product product,
            ReviewStatus status,
            Pageable pageable
    );

    /**
     * 특정 회원이 작성한 리뷰 목록을 조회
     * 생성일시 기준 내림차순 정렬
     *
     * @param member 조회할 회원
     * @param status 조회할 리뷰 상태
     * @return List<Review> 해당 회원의 리뷰 목록
     */
    List<Review> findByMemberAndStatusOrderByCreatedAtDesc(
            Member member,
            ReviewStatus status
    );

    /**
     * 특정 상품의 평균 평점을 계산
     * 승인된 리뷰만 대상으로 계산
     *
     * @param product 계산할 상품
     * @return Double 평균 평점
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product = :product AND r.status = 'APPROVED'")
    Double calculateAverageRating(Product product);

    /**
     * 특정 상품의 평점별 리뷰 수를 계산
     * 승인된 리뷰만 대상으로 계산
     *
     * @param product 계산할 상품
     * @return List<Object[]> 평점별 리뷰 수
     */
    @Query("SELECT r.rating, COUNT(r) FROM Review r " +
            "WHERE r.product = :product AND r.status = 'APPROVED' " +
            "GROUP BY r.rating")
    List<Object[]> countReviewsByRating(Product product);

    /**
     * 특정 주문 상품에 대한 리뷰 존재 여부 확인
     * 중복 리뷰 방지를 위해 사용
     *
     * @param orderItemId 확인할 주문 상품 ID
     * @param status 확인할 리뷰 상태
     * @return boolean 리뷰 존재 여부
     */
    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.orderItem.orderItemId = :orderItemId AND r.status = :status")
    boolean existsByOrderItemIdAndStatus(Long orderItemId, ReviewStatus status);
}
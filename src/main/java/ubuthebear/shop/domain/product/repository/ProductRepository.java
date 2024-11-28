package ubuthebear.shop.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.product.entity.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * 상품 데이터 접근을 위한 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 작업과 페이징 처리를 제공하며,
 * 추가적인 상품 검색 기능을 커스텀 메서드로 구현합니다.
 *
 * @author ubuthebear
 * @version 1.0
 * @see Product
 * @see JpaRepository
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * 상품명에 특정 문자열이 포함된 상품들을 검색
     * 대소문자를 구분하여 검색하며, 부분 일치도 검색 가능
     *
     * @param name 검색할 상품명 키워드
     * @return List<Product> 검색된 상품 목록 (검색 결과가 없을 경우 빈 리스트)
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Product> findByNameContaining(String name);
    /**
     * 특정 카테고리에 속한 모든 상품을 검색
     * Category 엔티티의 연관관계를 통해 검색
     *
     * @param categoryId 검색할 카테고리의 ID
     * @return List<Product> 해당 카테고리의 상품 목록 (없을 경우 빈 리스트)
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Product> findByCategoryCategoryId(Long categoryId);
    /**
     * 지정된 가격 범위 내의 상품들을 검색
     * 최소 가격과 최대 가격을 포함
     *
     * @param min 최소 가격 (이상)
     * @param max 최대 가격 (이하)
     * @return List<Product> 해당 가격 범위의 상품 목록 (없을 경우 빈 리스트)
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);

    /**
     * 재고가 특정 임계값 미만인 상품들을 검색
     * JPQL 쿼리를 사용하여 직접 구현된 메서드
     * 재고 관리와 부족 재고 알림에 사용
     *
     * @param threshold 재고 임계값
     * @return List<Product> 낮은 재고의 상품 목록 (없을 경우 빈 리스트)
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < :threshold")
    List<Product> findLowStockProducts(int threshold);
}
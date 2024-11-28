package ubuthebear.shop.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.product.entity.Category;

import java.util.Optional;

/**
 * 상품 카테고리 데이터 접근을 위한 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 작업과 페이징 처리를 제공합니다.
 *
 * @author ubuthebear
 * @version 1.0
 * @see Category
 * @see JpaRepository
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * 카테고리명으로 카테고리를 검색
     *
     * @param name 검색할 카테고리명
     * @return Optional<Category> 검색된 카테고리 (없을 경우 빈 Optional)
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    Optional<Category> findByName(String name);
    /**
     * 지정된 카테고리명이 이미 존재하는지 확인
     * 카테고리 생성 시 중복 검사에 사용
     *
     * @param name 중복 검사할 카테고리명
     * @return boolean 카테고리명 존재 여부 (존재하면 true)
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    boolean existsByName(String name);
}
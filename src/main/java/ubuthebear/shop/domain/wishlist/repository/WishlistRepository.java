package ubuthebear.shop.domain.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.product.entity.Product;
import ubuthebear.shop.domain.wishlist.entity.Wishlist;

import java.util.List;
import java.util.Optional;

/**
 * 위시리스트(찜 목록) 데이터 접근을 위한 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 처리
 * 위시리스트 관련 커스텀 쿼리 메서드를 제공
 *
 * @author ubuthebear
 * @version 1.0
 * @see Wishlist
 * @see Member
 * @see Product
 * @see JpaRepository
 */
@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    /**
     * 특정 회원의 위시리스트 목록을 조회
     * 회원이 찜한 모든 상품을 확인할 때 사용
     *
     * @param member 조회할 회원 엔티티
     * @return List<Wishlist> 해당 회원의 위시리스트 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Wishlist> findByMember(Member member);

    /**
     * 특정 회원의 특정 상품에 대한 위시리스트 항목을 조회
     * 위시리스트 항목 삭제나 상세 정보 조회 시 사용
     *
     * @param member 조회할 회원 엔티티
     * @param product 조회할 상품 엔티티
     * @return Optional<Wishlist> 해당 회원의 특정 상품 위시리스트 정보
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    Optional<Wishlist> findByMemberAndProduct(Member member, Product product);

    /**
     * 특정 회원이 특정 상품을 위시리스트에 추가했는지 확인
     * 중복 추가 방지나 찜 상태 확인 시 사용
     *
     * @param member 확인할 회원 엔티티
     * @param product 확인할 상품 엔티티
     * @return boolean 위시리스트 포함 여부 (true: 이미 추가됨, false: 추가되지 않음)
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    boolean existsByMemberAndProduct(Member member, Product product);
}
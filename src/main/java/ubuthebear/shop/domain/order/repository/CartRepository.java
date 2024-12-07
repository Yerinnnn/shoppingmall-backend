package ubuthebear.shop.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.order.entity.Cart;
import ubuthebear.shop.domain.product.entity.Product;

import java.util.List;
import java.util.Optional;

/**
 * 장바구니 데이터 접근을 위한 리포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 처리,
 * 장바구니 관련 커스텀 쿼리 메서드를 제공
 *
 * @author ubuthebear
 * @version 1.0
 * @see Cart
 * @see Member
 * @see Product
 * @see JpaRepository
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * 특정 회원의 장바구니 목록을 조회
     * 회원이 장바구니에 담은 모든 상품을 확인할 때 사용
     *
     * @param member 조회할 회원 엔티티
     * @return List<Cart> 해당 회원의 장바구니 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    List<Cart> findByMember(Member member);

    /**
     * 특정 회원의 특정 상품에 대한 장바구니 항목을 조회
     * 회원이 이미 장바구니에 담은 상품인지 확인
     * 수량을 업데이트할 때 사용
     *
     * @param member 조회할 회원 엔티티
     * @param product 조회할 상품 엔티티
     * @return Optional<Cart> 해당 회원의 특정 상품 장바구니 정보
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    Optional<Cart> findByMemberAndProduct(Member member, Product product);

    /**
     * 특정 회원의 장바구니를 전체 삭제
     * 주문 완료 후 장바구니 비우기나 회원 탈퇴 시 사용
     *
     * @param member 장바구니를 삭제할 회원 엔티티
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    void deleteByMember(Member member);
}
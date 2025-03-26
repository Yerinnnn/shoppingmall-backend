package ubuthebear.shop.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.cart.entity.CartItem;
import ubuthebear.shop.domain.member.entity.Member;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByMember(Member member);
    void deleteByMember(Member member);
}
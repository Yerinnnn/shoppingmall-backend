package ubuthebear.shop.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.repository.MemberRepository;
import ubuthebear.shop.domain.cart.dto.CartRequest;
import ubuthebear.shop.domain.cart.dto.CartResponse;
import ubuthebear.shop.domain.cart.entity.Cart;
import ubuthebear.shop.domain.cart.repository.CartRepository;
import ubuthebear.shop.domain.product.entity.Product;
import ubuthebear.shop.domain.product.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 장바구니 관리를 위한 서비스 클래스
 * 장바구니 항목의 추가, 조회, 수정, 삭제 등의 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // 기본적으로 읽기 전용 트랜잭션으로 설정
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * 장바구니에 상품을 추가
     * 이미 존재하는 상품인 경우 수량을 증가
     *
     * @param username 사용자명
     * @param request 장바구니 추가 요청 정보 (상품 ID, 수량)
     * @return CartResponse 추가된 장바구니 항목 정보
     * @throws RuntimeException 회원이나 상품을 찾을 수 없는 경우
     */
    @Transactional
    public CartResponse addToCart(String username, CartRequest request) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByMemberAndProduct(member, product)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setMember(member);
                    newCart.setProduct(product);
                    newCart.setQuantity(0);
                    return newCart;
                });

        cart.setQuantity(cart.getQuantity() + request.getQuantity());

        return new CartResponse(cartRepository.save(cart));
    }

    /**
     * 사용자의 장바구니 목록을 조회
     *
     * @param username 사용자명
     * @return List<CartResponse> 장바구니 항목 목록
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    public List<CartResponse> getCartItems(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return cartRepository.findByMember(member).stream()
                .map(CartResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 장바구니 항목의 수량을 수정
     * 권한 검증을 수행하여 본인의 장바구니만 수정 가능
     *
     * @param username 사용자명
     * @param cartId 장바구니 항목 ID
     * @param quantity 변경할 수량
     * @throws RuntimeException 장바구니 항목을 찾을 수 없거나 권한이 없는 경우
     */
    @Transactional
    public void updateCartItemQuantity(String username, Long cartId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cart.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        cart.setQuantity(quantity);
    }

    /**
     * 장바구니에서 특정 항목을 삭제
     * 권한 검증을 수행하여 본인의 장바구니만 삭제 가능
     *
     * @param username 사용자명
     * @param cartId 장바구니 항목 ID
     * @throws RuntimeException 장바구니 항목을 찾을 수 없거나 권한이 없는 경우
     */
    @Transactional
    public void removeFromCart(String username, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cart.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        cartRepository.delete(cart);
    }

    /**
     * 장바구니 비우기 기능
     * 사용자의 모든 장바구니 항목을 삭제
     *
     * @param username 사용자명
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    @Transactional
    public void clearCart(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        cartRepository.deleteByMember(member);
    }
}
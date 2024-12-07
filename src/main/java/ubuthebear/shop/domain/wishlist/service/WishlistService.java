package ubuthebear.shop.domain.wishlist.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.repository.MemberRepository;
import ubuthebear.shop.domain.wishlist.dto.WishlistResponse;
import ubuthebear.shop.domain.wishlist.entity.Wishlist;
import ubuthebear.shop.domain.wishlist.repository.WishlistRepository;
import ubuthebear.shop.domain.product.entity.Product;
import ubuthebear.shop.domain.product.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 위시리스트(찜 목록) 관리를 위한 서비스 클래스
 * 위시리스트 항목의 추가, 조회, 삭제 등의 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // 기본적으로 읽기 전용 트랜잭션으로 설정
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    /**
     * 위시리스트에 상품을 추가
     * 동일한 상품은 중복으로 추가할 수 없음
     *
     * @param username 사용자명
     * @param productId 추가할 상품 ID
     * @return WishlistResponse 추가된 위시리스트 항목 정보
     * @throws RuntimeException 회원이나 상품을 찾을 수 없는 경우, 또는 이미 위시리스트에 있는 경우
     */
    @Transactional
    public WishlistResponse addToWishlist(String username, Long productId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (wishlistRepository.existsByMemberAndProduct(member, product)) {
            throw new RuntimeException("Product already in wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setMember(member);
        wishlist.setProduct(product);

        return new WishlistResponse(wishlistRepository.save(wishlist));
    }

    /**
     * 사용자의 위시리스트 목록을 조회
     *
     * @param username 사용자명
     * @return List<WishlistResponse> 위시리스트 항목 목록
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    public List<WishlistResponse> getWishlist(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return wishlistRepository.findByMember(member).stream()
                .map(WishlistResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 위시리스트에서 특정 항목을 삭제
     * 권한 검증을 수행하여 본인의 위시리스트만 삭제 가능
     *
     * @param username 사용자명
     * @param wishlistId 위시리스트 항목 ID
     * @throws RuntimeException 위시리스트 항목을 찾을 수 없거나 권한이 없는 경우
     */
    @Transactional
    public void removeFromWishlist(String username, Long wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found"));

        if (!wishlist.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        wishlistRepository.delete(wishlist);
    }

    /**
     * 특정 상품이 사용자의 위시리스트에 포함되어 있는지 확인
     *
     * @param username 사용자명
     * @param productId 확인할 상품 ID
     * @return boolean 위시리스트 포함 여부
     * @throws RuntimeException 회원이나 상품을 찾을 수 없는 경우
     */
    public boolean isInWishlist(String username, Long productId) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return wishlistRepository.existsByMemberAndProduct(member, product);
    }
}
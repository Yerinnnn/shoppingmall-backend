package ubuthebear.shop.domain.wishlist.dto;

import lombok.Getter;
import ubuthebear.shop.domain.wishlist.entity.Wishlist;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 위시리스트 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * Wishlist 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see Wishlist
 */
@Getter
public class WishlistResponse {
    private final Long wishlistId;       // 위시리스트 아이템 고유 식별자
    private final Long productId;        // 상품 ID
    private final String productName;    // 상품명
    private final BigDecimal price;      // 상품 가격
    private final LocalDateTime addedAt; // 위시리스트 추가 일시

    /**
     * Wishlist 엔티티를 WishlistResponse DTO로 변환하는 생성자
     * 위시리스트에 담긴 상품의 정보를 클라이언트에게 제공하기 적합한 형태로 변환
     *
     * @param wishlist 변환할 Wishlist 엔티티
     */
    public WishlistResponse(Wishlist wishlist) {
        this.wishlistId = wishlist.getWishlistId();
        this.productId = wishlist.getProduct().getProductId();
        this.productName = wishlist.getProduct().getName();
        this.price = wishlist.getProduct().getPrice();
        this.addedAt = wishlist.getCreatedAt();
    }
}
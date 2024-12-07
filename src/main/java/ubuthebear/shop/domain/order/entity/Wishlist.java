package ubuthebear.shop.domain.order.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.product.entity.Product;

import java.time.LocalDateTime;

/**
 * 찜 목록(위시리스트) 정보를 저장하는 엔티티 클래스
 * 회원이 관심있는 상품을 저장하고 관리
 *
 * @author ubuthebear
 * @version 1.0
 * @see Member
 * @see Product
 */
@Entity
@Table(name = "wishlists",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "product_id"})  // 회원당 동일 상품은 한 번만 찜 가능
        })
@Getter @Setter
@NoArgsConstructor  // 파라미터가 없는 기본 생성자를 생성
@EntityListeners(AuditingEntityListener.class)  // JPA Auditing 기능을 사용하여 생성 시간 자동 관리
public class Wishlist {

    /**
     * 위시리스트 아이템의 고유 식별자
     * 자동 증가 전략을 사용하는 기본키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    /**
     * 위시리스트를 소유한 회원 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     * 회원(Member)과 다대일(N:1) 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 위시리스트에 담긴 상품 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     * 상품(Product)과 다대일(N:1) 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * 위시리스트 아이템 생성 일시
     * JPA Auditing을 통해 자동으로 설정
     * 한번 저장된 후에는 수정 불가
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
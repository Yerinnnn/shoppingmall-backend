package ubuthebear.shop.domain.cart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.product.entity.Product;

import java.time.LocalDateTime;

/**
 * 장바구니 상품 정보를 저장하는 엔티티 클래스
 * 회원별 장바구니에 담긴 상품과 수량 정보를 관리
 *
 * @author ubuthebear
 * @version 1.0
 * @see Member
 * @see Product
 */
@Entity
@Table(name = "cart_items")  // 테이블명을 cart_items로 지정
@Getter @Setter
@NoArgsConstructor  // 파라미터가 없는 기본 생성자를 생성
@EntityListeners(AuditingEntityListener.class)
public class Cart {

    /**
     * 장바구니 아이템의 고유 식별자
     * 자동 증가 전략을 사용하는 기본키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    /**
     * 장바구니를 소유한 회원 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     * 회원(Member)과 다대일(N:1) 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 장바구니에 담긴 상품 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     * 상품(Product)과 다대일(N:1) 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * 장바구니에 담긴 상품의 수량
     * null이 될 수 없음 (필수 입력 값)
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * 장바구니 아이템 생성 일시
     * JPA Auditing을 통해 자동으로 설정
     * 한번 저장된 후에는 수정 불가
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 장바구니 아이템 수정 일시
     * JPA Auditing을 통해 자동으로 업데이트
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
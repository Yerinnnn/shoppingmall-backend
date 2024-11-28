package ubuthebear.shop.domain.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 상품 정보를 저장하는 엔티티 클래스
 * 상품의 기본 정보와 재고 관리에 필요한 정보를 포함
 *
 * @author ubuthebear
 * @version 1.0
 * @see Category
 * @see ProductDetail
 */
@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {

    /**
     * 상품의 고유 식별자
     * 자동 생성되는 기본키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    /**
     * 상품명
     * null이 될 수 없음 (필수 입력 값)
     */
    @Column(nullable = false)
    private String name;

    /**
     * 썸네일 이미지
     * 상품 목록에서 보여질 대표 이미지
     */
    private String thumbnailUrl;

    /**
     * 상품 가격
     * BigDecimal을 사용하여 정확한 금액을 표현
     * null이 될 수 없음 (필수 입력 값)
     */
    @Column(nullable = false)
    private BigDecimal price;

    /**
     * 상품 설명
     * 선택적으로 입력 가능한 상품의 상세 설명
     */
    private String description;

    /**
     * 재고 수량
     * 상품의 현재 재고 수량
     * null이 될 수 없음 (필수 입력 값)
     */
    @Column(nullable = false)
    private Integer stockQuantity;

    /**
     * 상품이 속한 카테고리
     * FetchType.LAZY : 실제 사용시점에 카테고리 정보를 로딩
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * 상품의 상세 정보
     * 1:1 관계로 매핑
     * CascadeType.ALL : 상품이 삭제될 때 상세 정보도 함께 삭제
     */
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private ProductDetail productDetail;

    /**
     * 상품 등록일시
     * @CreatedDate : 엔티티 생성 시 자동으로 설정
     * updatable : 이후 수정할 수 없음
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 상품 수정일시
     * @LastModifiedDate : 엔티티가 수정될 때마다 자동으로 업데이트
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
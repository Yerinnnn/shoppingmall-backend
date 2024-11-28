package ubuthebear.shop.domain.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 상품 카테고리 정보를 저장하는 엔티티 클래스
 * 하나의 카테고리는 여러 상품을 포함할 수 있음
 *
 * @author ubuthebear
 * @version 1.0
 * @see Product
 */
@Entity
@Table(name = "categories")
@Getter @Setter
@NoArgsConstructor
public class Category {

    /**
     * 카테고리의 고유 식별자
     * 자동 생성되는 기본키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    /**
     * 카테고리명
     * null이 될 수 없음 (필수 입력 값)
     */
    @Column(nullable = false)
    private String name;

    /**
     * 카테고리 설명
     * 선택적으로 입력 가능한 부가 설명
     */
    private String description;

    /**
     * 해당 카테고리에 속한 상품 목록
     * Category와 Product는 1:N 관계
     * mappedBy = "category" : 연관관계의 주인이 Product 엔티티의 category 필드임
     */
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
}
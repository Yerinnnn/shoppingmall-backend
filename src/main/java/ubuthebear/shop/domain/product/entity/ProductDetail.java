package ubuthebear.shop.domain.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품의 상세 정보를 저장하는 엔티티 클래스
 * Product 엔티티와 1:1 관계를 가지며, 상품의 부가적인 정보를 저장
 *
 * @author ubuthebear
 * @version 1.0
 * @see Product
 */
@Entity
@Table(name = "product_details")
@Getter @Setter
@NoArgsConstructor
public class ProductDetail {

    /**
     * 상품 상세 정보의 고유 식별자
     * 자동 생성되는 기본키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDetailId;

    /**
     * 연관된 상품 정보
     * FetchType.LAZY : 실제 사용시점에 상품 정보를 로딩
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * 제조사명
     * 상품을 제조한 회사나 브랜드명을 저장
     */
    private String manufacturer;

    /**
     * 원산지 정보
     * 상품의 제조 또는 생산 국가/지역을 저장
     */
    private String origin;

    /**
     * 소재 정보
     * 상품의 주요 소재나 재질을 저장
     */
    private String material;

    /**
     * 크기 정보
     * 상품의 규격이나 치수를 저장
     */
    private String size;

    /**
     * 무게 정보
     * 상품의 무게를 저장
     */
    private String weight;

    /**
     * 상품 상세 설명 내용
     * HTML 형식으로 저장되며, 긴 텍스트를 저장하기 위해 TEXT 타입으로 정의
     * 상품의 특징, 사용법, 주의사항 등의 상세한 정보를 포함
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 상품 이미지 URL 목록
     * 여러 개의 상품 이미지 URL을 순서대로 저장
     * @ElementCollection: 별도의 엔티티가 아닌 값 타입 컬렉션으로 처리
     * @CollectionTable: 이미지 URL을 저장할 별도의 테이블 생성
     * @OrderColumn: 이미지 순서 유지를 위한 순서 컬럼 추가
     */
    @ElementCollection
    @CollectionTable(
            name = "product_detail_images",
            joinColumns = @JoinColumn(name = "product_detail_id")
    )
    @OrderColumn(name = "image_order")
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    /**
     * 상품 상세 페이지 조회수
     * 기본값 0으로 초기화
     * 상품 상세 페이지가 조회될 때마다 증가
     */
    @Column(name = "view_count")
    private Long viewCount = 0L;

    /**
     * 상세 정보 최종 수정 일시
     * @LastModifiedDate 어노테이션을 통해 엔티티가 수정될 때마다 자동으로 업데이트
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 상세 정보 최초 등록 일시
     * @CreatedDate : 엔티티 생성 시 자동으로 설정
     * updatable = false : 한번 저장된 후에는 수정 불가
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
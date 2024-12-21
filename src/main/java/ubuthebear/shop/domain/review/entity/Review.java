package ubuthebear.shop.domain.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.product.entity.Product;
import ubuthebear.shop.domain.order.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품 리뷰 정보를 저장하는 엔티티 클래스
 * 회원이 구매한 상품에 대한 평가와 리뷰를 관리
 *
 * @author ubuthebear
 * @version 1.0
 */
@Entity
@Table(name = "reviews")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Review {

    /**
     * 리뷰의 고유 식별자
     * 자동 증가 전략을 사용하는 기본키
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    /**
     * 리뷰를 작성한 회원 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     * 회원(Member)과 다대일(N:1) 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 리뷰 대상 상품 정보
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     * 상품(Product)과 다대일(N:1) 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /**
     * 리뷰와 연관된 주문 상품 정보
     * 구매 검증을 위해 사용
     * 일대일(1:1) 관계로 매핑
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    /**
     * 리뷰 평점 (1-5점)
     * null이 될 수 없음 (필수 입력 값)
     */
    @Column(nullable = false)
    private Integer rating;  // 1-5 평점

    /**
     * 리뷰 내용
     * null이 될 수 없음 (필수 입력 값)
     * 최대 1000자까지 저장 가능
     */
    @Column(nullable = false, length = 1000)
    private String content;

    /**
     * 리뷰 이미지 URL 목록
     * 여러 개의 이미지 URL을 순서대로 저장
     */
    @ElementCollection
    @CollectionTable(
            name = "review_images",
            joinColumns = @JoinColumn(name = "review_id")
    )
    @OrderColumn(name = "image_order")
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    /**
     * 리뷰 상태
     * PENDING: 승인 대기
     * APPROVED: 승인됨
     * REJECTED: 거절됨
     * DELETED: 삭제됨
     */
    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;

    /**
     * '도움이 됐어요' 수
     * 리뷰의 유용성을 나타내는 지표
     * 기본값 0으로 초기화
     */
    private Integer helpfulCount = 0;

    /**
     * 리뷰 생성 일시
     * JPA Auditing을 통해 자동으로 설정
     * 한번 저장된 후에는 수정 불가
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 리뷰 수정 일시
     * JPA Auditing을 통해 자동으로 업데이트
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 리뷰 삭제 일시
     * 실제 삭제가 아닌 논리적 삭제(soft delete) 구현을 위해 사용
     */
    private LocalDateTime deletedAt;
}
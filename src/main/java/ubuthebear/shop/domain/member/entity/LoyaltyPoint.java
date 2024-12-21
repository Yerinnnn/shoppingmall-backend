package ubuthebear.shop.domain.member.entity;

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
 * 회원의 포인트 잔액 정보를 관리하는 엔티티 클래스
 * 각 회원당 하나의 포인트 잔액 정보를 가지며, 포인트의 적립과 사용에 따른 잔액을 추적
 *
 * @author ubuthebear
 * @version 1.0
 * @see Member
 */
@Entity
@Table(name = "loyalty_points")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LoyaltyPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    /**
     * 포인트를 소유한 회원 정보
     * 회원과 1:1 관계로 매핑
     * 지연 로딩(LAZY)을 사용하여 성능 최적화
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 현재 포인트 잔액
     * BigDecimal을 사용하여 정확한 금액을 표현
     * null이 될 수 없으며, 기본값은 0
     */
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;  // 현재 포인트 잔액

    /**
     * 포인트 정보 생성 일시
     * JPA Auditing을 통해 자동으로 설정
     * 한번 저장된 후에는 수정 불가
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 포인트 정보 수정 일시
     * JPA Auditing을 통해 자동으로 업데이트
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
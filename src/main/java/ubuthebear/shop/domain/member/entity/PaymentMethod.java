package ubuthebear.shop.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 결제 수단 정보를 저장하는 엔티티 클래스
 * 회원의 결제 수단(신용카드, 계좌 등)을 관리
 */
@Entity
@Table(name = "payment_methods")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentMethodId;  // 결제 수단 고유 식별자

    /**
     * 결제 수단을 소유한 회원과의 다대일 관계
     * FetchType.LAZY: 실제 회원 정보가 필요할 때만 조회하여 성능 최적화
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // 회원 정보

    /**
     * 결제 수단 유형 (CREDIT_CARD: 신용카드, BANK_TRANSFER: 계좌이체 등)
     */
    @Column(nullable = false)
    private String paymentType;

    /**
     * 카드번호 (신용카드인 경우)
     * 보안을 위해 암호화하여 저장해야 함
     */
    private String cardNumber;

    /**
     * 카드 만료일 (신용카드인 경우)
     * 형식: MM/YY
     */
    private String expiryDate;

    /**
     * 기본 결제 수단 여부
     * true: 기본 결제 수단으로 설정
     * false: 일반 결제 수단
     */
    private boolean isDefault;

    /**
     * 엔티티 생성 일시
     * @CreatedDate: JPA Auditing을 통해 자동으로 설정
     */
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 엔티티 수정 일시
     * @LastModifiedDate: JPA Auditing을 통해 자동으로 업데이트
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
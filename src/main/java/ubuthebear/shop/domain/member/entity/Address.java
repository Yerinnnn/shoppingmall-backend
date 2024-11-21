package ubuthebear.shop.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    // FetchType.LAZY (지연 로딩): 데이터가 실제로 필요할 때만 가져오도록 설정
    // FetchType.EAGER (즉시 로딩): 연관된 데이터를 엔티티와 함께 즉시 가져오도록 설정
    // FetchType.LAZY를 사용할 때는 연관된 데이터를 사용할 시점에 영속성 컨텍스트(EntityManager)가 살아 있어야 함
    // 그렇지 않으면 LazyInitializationException이 발생할 수 있음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String address;
    private String city;
    private String postalCode;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

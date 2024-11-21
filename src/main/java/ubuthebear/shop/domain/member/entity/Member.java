package ubuthebear.shop.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "members")
@Getter @Setter
// @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성
// JPA 표준에서는 엔티티 클래스에 기본 생성자가 반드시 필요
// JPA는 리플렉션(Reflection)을 사용하여 엔티티 객체를 생성하기 때문에, 매개변수가 없는 기본 생성자가 없으면 엔티티를 초기화할 수 없음
@NoArgsConstructor
// @EntityListeners : JPA 엔티티의 특정 이벤트(저장, 수정 등)가 발생했을 때, 이를 감지하여 자동으로 특정 작업을 수행할 수 있도록 도와주는 기능
// JPA 엔티티의 변경 이벤트를 감지하여, 특정 동작(예: 시간 자동 기록)을 수행하는 리스너(listener)를 연결하는 데 사용
// 엔티티의 생성일시, 수정일시를 자동으로 관리해주는 JPA Auditing 기능
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    // @GeneratedValue : JPA(Java Persistence API)에서 데이터베이스의 기본 키(primary key) 값을 자동으로 생성해 주는 전략을 정의할 때 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String contact;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    // cascade : 부모 엔티티에 어떤 작업(저장, 삭제 등)을 하면, 연관된 자식 엔티티에게도 동일한 작업을 전파
    // CascadeType.ALL : 모든 작업(저장, 병합, 제거, 새로고침, 분리)을 자식 엔티티에 전파
    // 장점 : 부모-자식 관계에서의 작업 단순화, 데이터의 일관성 유지
    // 주의할 점 : 논리적 의존성을 잘 따져서 사용해야 함
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    /*
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private LoyaltyPoints loyaltyPoints;

    // 주문 내역(Order)이나 리뷰(ProductReview)는 회원(Member)와 연관되어 있지만,
    // 회원이 삭제된다고 해서 해당 주문 내역이나 리뷰가 삭제되어야 하는 것은 아님
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ProductReview> reviews = new ArrayList<>();
     */

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_roles",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}

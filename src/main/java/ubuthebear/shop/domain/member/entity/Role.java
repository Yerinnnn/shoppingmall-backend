package ubuthebear.shop.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 사용자 역할(권한) 정보를 저장하는 엔티티 클래스
 * Spring Security의 권한 관리에 사용
 * 예: ROLE_USER, ROLE_ADMIN 등
 */
@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor  // JPA는 기본 생성자가 필요함
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;  // 역할 고유 식별자

    /**
     * 역할 이름
     * Spring Security에서 사용하는 형식: "ROLE_" 접두사 + 역할명
     * 예: ROLE_USER, ROLE_ADMIN
     * unique = true: 역할 이름은 중복될 수 없음
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 역할 설명
     * 해당 역할에 대한 부가 설명 정보
     */
    @Column
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<Member> members;

    /**
     * 역할 생성 메서드
     * @param name 역할 이름 (예: "ROLE_USER")
     * @return 생성된 Role 객체
     */
    public static Role createRole(String name) {
        Role role = new Role();
        role.name = name;
        return role;
    }
}
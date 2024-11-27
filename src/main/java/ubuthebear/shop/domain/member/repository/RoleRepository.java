package ubuthebear.shop.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.member.entity.Role;

import java.util.Optional;

// Role 엔티티에 대한 데이터베이스 작업을 처리하는 리포지토리 인터페이스
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // 이름으로 Role을 검색하는 메서드
    // Optional을 사용하여 null 처리를 안전하게 함
    Optional<Role> findByName(String name);
}
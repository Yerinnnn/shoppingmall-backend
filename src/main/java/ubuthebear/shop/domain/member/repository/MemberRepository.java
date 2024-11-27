package ubuthebear.shop.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.member.entity.Member;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 회원 정보에 대한 데이터베이스 접근을 담당하는 리포지토리
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 수행
 *
 * @see JpaRepository
 * @see Member
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 사용자명으로 회원을 조회
     * 로그인, 회원가입 시 사용자 존재 여부 확인에 사용
     *
     * @param username 조회할 사용자명
     * @return Optional<Member> 조회된 회원 정보를 Optional로 감싸서 반환
     */
    Optional<Member> findByUsername(String username);

    /**
     * 사용자명의 존재 여부를 확인
     * 회원가입 시 중복 아이디 검사에 사용
     *
     * @param username 확인할 사용자명
     * @return boolean 사용자명 존재 여부
     */
    boolean existsByUsername(String username);

    /**
     * 연락처로 회원을 조회
     * 회원가입 시 중복 연락처 검사 등에 사용
     *
     * @param contact 조회할 연락처
     * @return Optional<Member> 조회된 회원 정보를 Optional로 감싸서 반환
     */
    Optional<Member> findByContact(String contact);

    /**
     * 이름과 연락처로 회원을 조회
     * 아이디 찾기 등의 기능에 사용
     *
     * @param name 회원 이름
     * @param contact 연락처
     * @return Optional<Member> 조회된 회원 정보를 Optional로 감싸서 반환
     */
    Optional<Member> findByNameAndContact(String name, String contact);

    /**
     * 특정 날짜 이후에 가입한 회원 수를 조회
     * 통계 정보 제공 등에 사용
     *
     * @param createdAt 기준 날짜
     * @return long 조회된 회원 수
     */
    long countByCreatedAtAfter(LocalDateTime createdAt);
}
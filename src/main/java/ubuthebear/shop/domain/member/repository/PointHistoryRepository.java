package ubuthebear.shop.domain.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.entity.PointHistory;

import java.time.LocalDateTime;

/**
 * 포인트 변동 이력에 대한 데이터베이스 접근을 담당하는 리포지토리
 * JpaRepository를 상속받아 기본적인 CRUD 작업을 수행하고,
 * 포인트 이력 조회를 위한 커스텀 쿼리 메서드를 제공
 *
 * @author ubuthebear
 * @version 1.0
 * @see PointHistory
 * @see Member
 */
@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    /**
     * 특정 회원의 전체 포인트 이력을 최신순으로 페이징하여 조회
     *
     * @param member 조회할 회원 엔티티
     * @param pageable 페이징 정보
     * @return Page<PointHistory> 페이징된 포인트 이력 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    Page<PointHistory> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

    /**
     * 특정 회원의 포인트 이력을 기간별로 조회
     * 지정된 시작일과 종료일 사이의 이력을 최신순으로 정렬하여 반환
     *
     * @param member 조회할 회원 엔티티
     * @param startDate 조회 시작일시
     * @param endDate 조회 종료일시
     * @param pageable 페이징 정보
     * @return Page<PointHistory> 페이징된 포인트 이력 목록
     * @throws org.springframework.dao.DataAccessException 데이터베이스 접근 중 오류 발생 시
     */
    Page<PointHistory> findByMemberAndCreatedAtBetweenOrderByCreatedAtDesc(
            Member member,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );
}
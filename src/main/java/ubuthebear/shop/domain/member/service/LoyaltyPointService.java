package ubuthebear.shop.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.member.dto.PointBalanceResponse;
import ubuthebear.shop.domain.member.dto.PointHistoryResponse;
import ubuthebear.shop.domain.member.entity.*;
import ubuthebear.shop.domain.member.repository.LoyaltyPointRepository;
import ubuthebear.shop.domain.member.repository.MemberRepository;
import ubuthebear.shop.domain.member.repository.PointHistoryRepository;
import ubuthebear.shop.domain.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 포인트 관리를 위한 서비스 클래스
 * 포인트의 적립, 사용, 조회 등 포인트와 관련된 모든 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoyaltyPointService {
    private final LoyaltyPointRepository loyaltyPointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;

    /**
     * 포인트를 적립하는 메서드
     * 회원의 포인트 잔액을 증가시키고 적립 이력을 기록
     * 동시성 제어를 위해 잠금(Lock)을 사용
     *
     * @param username 포인트를 적립할 회원의 사용자명
     * @param amount 적립할 포인트 금액
     * @param description 적립 내용 설명
     * @param order 연관된 주문 정보 (없을 수 있음)
     * @return PointBalanceResponse 적립 후 포인트 잔액 정보
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    @Transactional
    public PointBalanceResponse earnPoints(String username, BigDecimal amount, String description, Order order) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        LoyaltyPoint point = loyaltyPointRepository.findByMemberWithLock(member)
                .orElseGet(() -> {
                    LoyaltyPoint newPoint = new LoyaltyPoint();
                    newPoint.setMember(member);
                    return newPoint;
                });

        // 포인트 증가
        point.setBalance(point.getBalance().add(amount));
        LoyaltyPoint savedPoint = loyaltyPointRepository.save(point);

        // 포인트 이력 저장
        PointHistory history = new PointHistory();
        history.setMember(member);
        history.setAmount(amount);
        history.setType(PointType.EARN);
        history.setDescription(description);
        history.setOrder(order);
        history.setBalanceAfter(savedPoint.getBalance());
        pointHistoryRepository.save(history);

        return new PointBalanceResponse(savedPoint);
    }

    /**
     * 포인트를 사용하는 메서드
     * 회원의 포인트 잔액을 차감하고 사용 이력을 기록
     * 잔액 부족 시 예외 발생
     *
     * @param username 포인트를 사용할 회원의 사용자명
     * @param amount 사용할 포인트 금액
     * @param description 사용 내용 설명
     * @param order 연관된 주문 정보 (없을 수 있음)
     * @return PointBalanceResponse 사용 후 포인트 잔액 정보
     * @throws RuntimeException 회원을 찾을 수 없거나 잔액이 부족한 경우
     */
    @Transactional
    public PointBalanceResponse usePoints(String username, BigDecimal amount, String description, Order order) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        LoyaltyPoint point = loyaltyPointRepository.findByMemberWithLock(member)
                .orElseThrow(() -> new RuntimeException("No points available"));

        if (point.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient points");
        }

        // 포인트 차감
        point.setBalance(point.getBalance().subtract(amount));
        LoyaltyPoint savedPoint = loyaltyPointRepository.save(point);

        // 포인트 이력 저장
        PointHistory history = new PointHistory();
        history.setMember(member);
        history.setAmount(amount.negate());
        history.setType(PointType.USE);
        history.setDescription(description);
        history.setOrder(order);
        history.setBalanceAfter(savedPoint.getBalance());
        pointHistoryRepository.save(history);

        return new PointBalanceResponse(savedPoint);
    }

    /**
     * 사용한 포인트를 취소하고 반환하는 메서드
     * 주문 취소 등의 경우에 사용된 포인트를 다시 회원에게 돌려줌
     *
     * @param username 포인트를 반환받을 회원의 사용자명
     * @param amount 반환할 포인트 금액
     * @param description 취소 내용 설명
     * @param order 연관된 주문 정보 (없을 수 있음)
     * @return PointBalanceResponse 반환 후 포인트 잔액 정보
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    @Transactional
    public PointBalanceResponse cancelPointUse(String username, BigDecimal amount, String description, Order order) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        LoyaltyPoint point = loyaltyPointRepository.findByMemberWithLock(member)
                .orElseThrow(() -> new RuntimeException("No points available"));

        // 포인트 반환
        point.setBalance(point.getBalance().add(amount));
        LoyaltyPoint savedPoint = loyaltyPointRepository.save(point);

        // 포인트 이력 저장
        PointHistory history = new PointHistory();
        history.setMember(member);
        history.setAmount(amount);
        history.setType(PointType.CANCEL);
        history.setDescription(description);
        history.setOrder(order);
        history.setBalanceAfter(savedPoint.getBalance());
        pointHistoryRepository.save(history);

        return new PointBalanceResponse(savedPoint);
    }

    /**
     * 회원의 현재 포인트 잔액을 조회하는 메서드
     * 포인트 정보가 없는 경우 새로 생성
     *
     * @param username 조회할 회원의 사용자명
     * @return PointBalanceResponse 현재 포인트 잔액 정보
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    public PointBalanceResponse getPointBalance(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        LoyaltyPoint point = loyaltyPointRepository.findByMember(member)
                .orElseGet(() -> {
                    LoyaltyPoint newPoint = new LoyaltyPoint();
                    newPoint.setMember(member);
                    newPoint.setBalance(BigDecimal.ZERO);
                    return loyaltyPointRepository.save(newPoint);
                });

        return new PointBalanceResponse(point);
    }

    /**
     * 회원의 포인트 변동 이력을 조회하는 메서드
     * 페이징 처리를 통해 대량의 데이터를 효율적으로 조회
     *
     * @param username 조회할 회원의 사용자명
     * @param pageable 페이징 정보
     * @return Page<PointHistoryResponse> 페이징된 포인트 이력 목록
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    public Page<PointHistoryResponse> getPointHistory(String username, Pageable pageable) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return pointHistoryRepository.findByMemberOrderByCreatedAtDesc(member, pageable)
                .map(PointHistoryResponse::new);
    }

    /**
     * 특정 기간 동안의 포인트 변동 이력을 조회하는 메서드
     *
     * @param username 조회할 회원의 사용자명
     * @param startDate 조회 시작 일시
     * @param endDate 조회 종료 일시
     * @param pageable 페이징 정보
     * @return Page<PointHistoryResponse> 페이징된 포인트 이력 목록
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    public Page<PointHistoryResponse> getPointHistoryByPeriod(
            String username, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return pointHistoryRepository
                .findByMemberAndCreatedAtBetweenOrderByCreatedAtDesc(member, startDate, endDate, pageable)
                .map(PointHistoryResponse::new);
    }

    /**
     * 관리자가 회원의 포인트를 수동으로 조정하는 메서드
     * 시스템 오류 수정이나 고객 서비스 목적으로 사용
     *
     * @param username 조정할 회원의 사용자명
     * @param amount 조정할 포인트 금액 (양수: 증가, 음수: 감소)
     * @param description 조정 사유
     * @return PointBalanceResponse 조정 후 포인트 잔액 정보
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    @Transactional
    public PointBalanceResponse adjustPoints(String username, BigDecimal amount, String description) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        LoyaltyPoint point = loyaltyPointRepository.findByMemberWithLock(member)
                .orElseGet(() -> {
                    LoyaltyPoint newPoint = new LoyaltyPoint();
                    newPoint.setMember(member);
                    return newPoint;
                });

        // 포인트 조정
        point.setBalance(point.getBalance().add(amount));
        LoyaltyPoint savedPoint = loyaltyPointRepository.save(point);

        // 포인트 이력 저장
        PointHistory history = new PointHistory();
        history.setMember(member);
        history.setAmount(amount);
        history.setType(PointType.ADJUST);
        history.setDescription(description);
        history.setBalanceAfter(savedPoint.getBalance());
        pointHistoryRepository.save(history);

        return new PointBalanceResponse(savedPoint);
    }

    /**
     * 주문 금액에 따른 적립 예정 포인트를 계산하는 메서드
     * 현재는 주문 금액의 1%를 적립하는 정책 사용
     *
     * @param orderAmount 주문 금액
     * @return BigDecimal 적립 예정 포인트
     */
    public BigDecimal calculateEarnablePoints(BigDecimal orderAmount) {
        // 예: 주문금액의 1% 적립
        return orderAmount.multiply(new BigDecimal("0.01")).setScale(0, java.math.RoundingMode.FLOOR);
    }
}
package ubuthebear.shop.domain.member.dto;

import lombok.Getter;
import ubuthebear.shop.domain.member.entity.LoyaltyPoint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 포인트 잔액 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * LoyaltyPoint 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see LoyaltyPoint
 */
@Getter
public class PointBalanceResponse {

    /**
     * 포인트 소유 회원의 ID
     */
    private final Long memberId;

    /**
     * 현재 포인트 잔액
     * BigDecimal을 사용하여 정확한 금액을 표현
     */
    private final BigDecimal balance;

    /**
     * 포인트 잔액 최종 업데이트 시간
     */
    private final LocalDateTime updatedAt;

    /**
     * LoyaltyPoint 엔티티를 PointBalanceResponse DTO로 변환하는 생성자
     *
     * @param point 변환할 LoyaltyPoint 엔티티
     */
    public PointBalanceResponse(LoyaltyPoint point) {
        this.memberId = point.getMember().getMemberId();
        this.balance = point.getBalance();
        this.updatedAt = point.getUpdatedAt();
    }
}
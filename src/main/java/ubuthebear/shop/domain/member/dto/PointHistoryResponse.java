package ubuthebear.shop.domain.member.dto;

import lombok.Getter;
import ubuthebear.shop.domain.member.entity.PointHistory;
import ubuthebear.shop.domain.member.entity.PointType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 포인트 변동 이력 정보를 반환하기 위한 응답 DTO (Data Transfer Object) 클래스
 * PointHistory 엔티티의 정보를 클라이언트에게 전달하는 용도로 사용
 *
 * @author ubuthebear
 * @version 1.0
 * @see PointHistory
 * @see PointType
 */
@Getter
public class PointHistoryResponse {

    /**
     * 포인트 이력의 고유 식별자
     */
    private final Long historyId;

    /**
     * 포인트 변동 금액
     * 적립의 경우 양수, 사용의 경우 음수
     */
    private final BigDecimal amount;

    /**
     * 포인트 변동 유형
     * EARN(적립), USE(사용), EXPIRE(만료), CANCEL(취소), ADJUST(조정)
     */
    private final PointType type;

    /**
     * 포인트 변동에 대한 설명
     * 예: "상품 구매 적립", "주문 취소 환불" 등
     */
    private final String description;

    /**
     * 변동 후 포인트 잔액
     * 해당 변동이 발생한 후의 최종 잔액
     */
    private final BigDecimal balanceAfter;

    /**
     * 포인트 변동이 발생한 시간
     */
    private final LocalDateTime createdAt;

    /**
     * PointHistory 엔티티를 PointHistoryResponse DTO로 변환하는 생성자
     *
     * @param history 변환할 PointHistory 엔티티
     */
    public PointHistoryResponse(PointHistory history) {
        this.historyId = history.getHistoryId();
        this.amount = history.getAmount();
        this.type = history.getType();
        this.description = history.getDescription();
        this.balanceAfter = history.getBalanceAfter();
        this.createdAt = history.getCreatedAt();
    }
}

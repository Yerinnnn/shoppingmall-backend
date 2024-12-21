package ubuthebear.shop.domain.review.entity;

/**
 * 리뷰의 상태를 나타내는 열거형 클래스
 * 리뷰의 현재 처리 상태를 표현
 *
 * @author ubuthebear
 * @version 1.0
 */
public enum ReviewStatus {
    PENDING,    // 리뷰 승인 대기
    APPROVED,   // 승인됨
    REJECTED,   // 거절됨
    DELETED     // 삭제됨
}
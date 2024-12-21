package ubuthebear.shop.domain.member.entity;

/**
 * 포인트 이력의 유형을 나타내는 열거형
 */
public enum PointType {
    EARN("적립"),         // 포인트 적립
    USE("사용"),          // 포인트 사용
    EXPIRE("만료"),       // 포인트 만료
    CANCEL("취소"),       // 사용 취소
    ADJUST("조정");       // 관리자 조정

    private final String description;

    PointType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
package ubuthebear.shop.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 시 클라이언트로부터 받는 데이터를 담는 DTO
 * 새로운 사용자 등록에 필요한 모든 정보를 포함
 */
// @Data
// getter, setter, equals, hashCode, toString 메서드 자동 생성
@Data
// @AllArgsConstructor
// 모든 필드를 매개변수로 받는 생성자 자동 생성
@AllArgsConstructor
// @NoArgsConstructor
// 매개변수 없는 기본 생성자 자동 생성
// JSON 데이터를 LoginRequest 객체로 변환할 때 필요 (LoginRequest.java 주석 참고)
@NoArgsConstructor
public class SignupRequest {
    private String username;  // 가입할 사용자의 아이디
    private String password;  // 가입할 사용자의 비밀번호
    private String name;      // 가입할 사용자의 실제 이름
    private String contact;   // 가입할 사용자의 연락처 정보
}
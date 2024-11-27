package ubuthebear.shop.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인증 성공 후 클라이언트에게 반환되는 응답 DTO
 * 로그인이나 회원가입 성공 시 JWT 토큰과 사용자 정보를 담아 반환
 */
// @Data
// getter, setter, equals, hashCode, toString 메서드 자동 생성
@Data
// @AllArgsConstructor
// 모든 필드를 매개변수로 받는 생성자 자동 생성
// JWT 토큰과 username을 한 번에 설정하기 위해 사용
@AllArgsConstructor
// @NoArgsConstructor
// 매개변수 없는 기본 생성자 자동 생성
// JSON 데이터를 LoginRequest 객체로 변환할 때 필요 (LoginRequest.java 주석 참고)
@NoArgsConstructor
public class AuthResponse {
    private String token;     // JWT 인증 토큰
    private String username;  // 인증된 사용자의 아이디
}
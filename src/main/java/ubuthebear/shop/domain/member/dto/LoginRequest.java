package ubuthebear.shop.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 시 클라이언트로부터 받는 데이터를 담는 DTO
 * 사용자의 로그인 시도 시 필요한 정보를 포함
 */
// @Data
// getter, setter, equals, hashCode, toString 메서드 자동 생성
@Data
// @AllArgsConstructor
// 모든 필드를 매개변수로 받는 생성자 자동 생성
// 로그인 시 username과 password를 한 번에 설정하기 위해 사용
@AllArgsConstructor
// @NoArgsConstructor
// 매개변수 없는 기본 생성자 자동 생성
// JSON 데이터를 LoginRequest 객체로 변환할 때 필요
// 클라이언트가 JSON으로 보낸 데이터 : { "username": "user123", "password": "pass123" }
// 이 JSON을 Spring이 LoginRequest 객체로 자동 변환할 때 기본 생성자가 필요
// Spring이 내부적으로 이 데이터를 LoginRequest 객체로 변환하는 과정
// LoginRequest request = new LoginRequest();  // 1. 기본 생성자로 빈 객체 생성
// request.setUsername("user123");             // 2. username 설정
// request.setPassword("pass123");             // 3. password 설정
// Spring은 클라이언트가 보낸 JSON 데이터를 Java 객체로 변환할 때 (역직렬화) 기본 생성자로 객체를 만든 후, 각 필드값을 설정함
// 기본 생성자가 없으면 이 과정에서 오류가 발생하게 됨
@NoArgsConstructor
public class LoginRequest {
    private String username;  // 로그인할 사용자의 아이디
    private String password;  // 로그인할 사용자의 비밀번호
}
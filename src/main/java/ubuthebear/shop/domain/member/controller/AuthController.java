package ubuthebear.shop.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ubuthebear.shop.domain.member.dto.AuthResponse;
import ubuthebear.shop.domain.member.dto.LoginRequest;
import ubuthebear.shop.domain.member.dto.SignupRequest;
import ubuthebear.shop.domain.member.service.AuthService;

@RestController  // REST API 컨트롤러임을 나타냄
@RequestMapping("/api/auth")  // 기본 URL 경로 설정
@RequiredArgsConstructor  // final 필드에 대한 생성자 자동 생성
public class AuthController {

    private final AuthService authService;  // 인증 관련 비즈니스 로직 처리 서비스

    /**
     * 회원가입 API 엔드포인트
     * @param request 회원가입 요청 정보
     * @return 회원가입 결과 응답
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    /**
     * 로그인 API 엔드포인트
     * @param request 로그인 요청 정보
     * @return 로그인 결과 및 JWT 토큰 응답
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
package ubuthebear.shop.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.member.dto.MemberResponse;
import ubuthebear.shop.domain.member.service.MemberService;

@RestController  // REST API 컨트롤러임을 나타냄
@RequestMapping("/api/members")  // 기본 URL 경로 설정
@RequiredArgsConstructor  // final 필드에 대한 생성자 자동 생성
public class MemberController {

    private final MemberService memberService;  // 회원 관련 비즈니스 로직 처리 서비스

    /**
     * 현재 로그인한 사용자의 정보를 조회하는 API 엔드포인트
     * @param authentication 현재 인증 정보 (Spring Security가 자동 주입)
     * @return 현재 로그인한 사용자의 정보
     */
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(Authentication authentication) {
        return ResponseEntity.ok(memberService.getMyInfo(authentication.getName()));
    }
}
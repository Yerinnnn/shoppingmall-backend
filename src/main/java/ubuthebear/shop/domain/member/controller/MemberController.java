package ubuthebear.shop.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.member.dto.AddressRequest;
import ubuthebear.shop.domain.member.dto.AddressResponse;
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

    /**
     * 새로운 배송지를 추가하는 API 엔드포인트
     * POST /api/members/me/addresses
     * @param authentication 인증 정보
     * @param request 배송지 정보
     * @return 저장된 배송지 정보
     */
    @PostMapping("/me/addresses")
    public ResponseEntity<AddressResponse> addAddress(
            Authentication authentication,
            @RequestBody AddressRequest request) {
        return ResponseEntity.ok(memberService.addAddress(authentication.getName(), request));
    }

    /**
     * 배송지를 삭제하는 API 엔드포인트
     * DELETE /api/members/me/addresses/{addressId}
     * @param authentication 인증 정보
     * @param addressId 삭제할 배송지 ID
     * @return 처리 결과
     */
    @DeleteMapping("/me/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            Authentication authentication,
            @PathVariable Long addressId) {
        memberService.deleteAddress(authentication.getName(), addressId);
        return ResponseEntity.ok().build();
    }
}
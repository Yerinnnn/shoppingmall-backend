package ubuthebear.shop.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ubuthebear.shop.domain.member.dto.MemberResponse;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.repository.MemberRepository;

@Service  // 스프링의 서비스 계층 컴포넌트임을 나타냄
@RequiredArgsConstructor  // final 필드에 대한 생성자 자동 생성
public class MemberService {

    private final MemberRepository memberRepository;  // 회원 정보 DB 조작을 위한 레포지토리

    /**
     * 현재 로그인한 사용자의 정보를 조회하는 메서드
     * @param username 사용자 아이디
     * @return 사용자 엔티티
     * @throws RuntimeException 사용자를 찾을 수 없는 경우 발생
     */
    public MemberResponse getMyInfo(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        // DTO 반환의 장점
        // 필요한 정보만 선택적으로 전달
        // 민감한 정보 제외 가능
        // 데이터 변환/가공이 용이
        // API 스펙 변경이 엔티티에 영향을 주지 않음
        return new MemberResponse(member);
    }
}
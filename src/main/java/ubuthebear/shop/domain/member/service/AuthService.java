package ubuthebear.shop.domain.member.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ubuthebear.shop.domain.member.dto.AuthResponse;
import ubuthebear.shop.domain.member.dto.LoginRequest;
import ubuthebear.shop.domain.member.dto.SignupRequest;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.entity.Role;
import ubuthebear.shop.domain.member.repository.MemberRepository;
import ubuthebear.shop.domain.member.repository.RoleRepository;
import ubuthebear.shop.global.jwt.JwtTokenProvider;

import java.util.Collections;

@Service  // 스프링의 서비스 계층 컴포넌트임을 나타냄
@RequiredArgsConstructor  // final 필드에 대한 생성자 자동 생성
@Transactional  // 모든 메서드를 트랜잭션으로 처리
public class AuthService {
    // 필요한 의존성 주입
    private final MemberRepository memberRepository;     // 회원 정보 DB 조작
    private final RoleRepository roleRepository;         // 역할 정보 DB 조작
    private final PasswordEncoder passwordEncoder;       // 비밀번호 암호화 처리
    private final AuthenticationManagerBuilder authenticationManagerBuilder;  // 인증 관리자 생성
    private final JwtTokenProvider jwtTokenProvider;     // JWT 토큰 생성 및 검증

    /**
     * 회원가입 처리 메서드
     * @param request 회원가입 요청 정보
     * @return 회원가입 결과 및 사용자 정보
     * @throws RuntimeException 이미 존재하는 사용자인 경우 발생
     */
    public AuthResponse signup(SignupRequest request) {
        // 중복 사용자 검증
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 존재하는 사용자입니다");
        }

        // 새로운 회원 엔티티 생성 및 정보 설정
        Member member = new Member();
        member.setUsername(request.getUsername());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setName(request.getName());
        member.setContact(request.getContact());

        // 기본 사용자 역할 설정
        // 1. "ROLE_USER"라는 이름의 Role을 DB에서 찾음
        Role userRole = roleRepository.findByName("ROLE_USER")
                // 2. 만약 없다면(.orElseGet) 새로 생성해서 DB에 저장
                .orElseGet(() -> roleRepository.save(Role.createRole("ROLE_USER")));

        // 3. 회원에게 단일 권한 부여
        // Collections.singleton(): 단일 요소를 가진 Set을 생성
        member.setRoles(Collections.singleton(userRole));  // 단일 역할 설정
        memberRepository.save(member);  // DB에 저장

        return new AuthResponse(
                "회원가입 성공",
                request.getUsername()
        );
    }

    /**
     * 로그인 처리 메서드
     * @param request 로그인 요청 정보
     * @return 로그인 결과 및 JWT 토큰
     * @throws RuntimeException 로그인 실패 시 발생
     */
    public AuthResponse login(LoginRequest request) {
        try {
            // 사용자 조회
            Member member = memberRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

            // 비밀번호 검증
            if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
                throw new RuntimeException("비밀번호가 일치하지 않습니다");
            }

            // JWT 토큰 생성 및 반환
            String token = jwtTokenProvider.generateToken(member.getUsername());
            return new AuthResponse(token, member.getUsername());
        } catch (Exception e) {
            System.out.println("로그인 에러: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
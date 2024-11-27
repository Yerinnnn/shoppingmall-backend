package ubuthebear.shop.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.repository.MemberRepository;

import java.util.Collections;

/**
 * Spring Security에서 사용자 인증을 처리하는 서비스
 * UserDetailsService 인터페이스를 구현하여 사용자 정보를 로드
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 사용자명으로 사용자의 정보를 조회
     * Spring Security에서 인증 과정에서 사용
     *
     * @param username 조회할 사용자명
     * @return UserDetails Spring Security에서 사용할 수 있는 사용자 정보
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우 발생
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // memberRepository를 통해 사용자명으로 회원 정보 조회
        // Optional을 사용하여 null 처리를 안전하게 함
        return memberRepository.findByUsername(username)
                // 찾은 Member 엔티티를 UserDetails 객체로 변환
                .map(this::createUserDetails)
                // 사용자를 찾지 못한 경우 예외 발생
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
    }

    /**
     * Member 엔티티를 Spring Security의 UserDetails 객체로 변환
     *
     * @param member 변환할 회원 엔티티
     * @return UserDetails Spring Security에서 사용할 수 있는 사용자 정보
     */
    private UserDetails createUserDetails(Member member) {
        // 기본적으로 'ROLE_USER' 권한을 가진 SimpleGrantedAuthority 생성
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        // Spring Security의 User 객체 생성
        // 사용자명, 비밀번호, 권한 정보를 포함
        return org.springframework.security.core.userdetails.User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                // singleton 리스트로 권한 설정
                .authorities(Collections.singleton(authority))
                .build();
    }
}
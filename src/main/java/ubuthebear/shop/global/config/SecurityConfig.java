package ubuthebear.shop.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ubuthebear.shop.global.jwt.JwtAuthenticationFilter;
import ubuthebear.shop.global.jwt.JwtTokenProvider;

import java.util.Arrays;

// 스프링 시큐리티 설정 클래스임을 나타냄
// @Configuration
// Spring 컨테이너에 의해 설정 정보를 제공, 빈(Bean)을 정의하고 관리하는 데 사용
// @Configuration은 스프링의 설정 클래스임을 나타내며, 보안과 같은 핵심 기능은 일반적으로 설정 클래스로 관리
// SecurityConfig의 경우, 스프링 시큐리티의 복잡한 설정을 담당하기 때문에 @Configuration을 사용하고,
// 필터체인이나 비밀번호 인코더와 같은 핵심 컴포넌트들을 @Bean으로 등록
// 설정의 중앙화(보안 관련 설정을 한 곳에서 관리), 설정의 모듈화(다른 설정들과 분리하여 관리), Bean 설정 최적화(@Configuration이 붙은 클래스는 CGLIB을 통해 프록시가 생성되어, Bean들의 싱글톤 상태를 보장)
@Configuration
// 웹 보안 활성화
@EnableWebSecurity
public class SecurityConfig {
    // 스프링 시큐리티 필터 체인 구성
    // 필터 체인
    // HTTP 요청이 애플리케이션에 도달하기 전에 거치는 일련의 필터들을 의미
    // 예: 인증 필터 → JWT 토큰 검증 필터 → 권한 검사 필터 등의 순서로 요청을 처리
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // /api/** 경로에 대해서만 보안 설정 적용
                // REST API 엔드포인트를 의미
                // 정적 리소스(HTML, CSS, JS 등)는 다른 경로(/static/** 등)에서 처리
                // API 요청에는 JWT 기반 인증, 정적 리소스는 다른 보안 정책 적용 가능
                .securityMatcher("/api/**")
                // CSRF 보호 기능 비활성화
                // REST API에서는 CSRF 공격 위험이 적고, JWT를 사용하므로 비활성화
                .csrf(csrf -> csrf.disable())
                // HTTP 요청에 대한 권한 설정
                .authorizeHttpRequests(auth ->
                        // /api/auth/** 경로는 모든 사용자에게 허용
                        // 로그인, 회원가입 등의 인증 관련 API는 인증 없이 접근 가능해야 함
                        auth.requestMatchers("/api/auth/**").permitAll()
                                // 그 외 모든 요청도 일단 허용
                                // 개발 초기 단계에서는 모든 요청을 허용하고,
                                // 추후 필요한 엔드포인트에 대해 보안을 적용하는 것이 일반적
                                .anyRequest().permitAll()
                );

        return http.build();
    }

    // 비밀번호 암호화를 위한 인코더 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
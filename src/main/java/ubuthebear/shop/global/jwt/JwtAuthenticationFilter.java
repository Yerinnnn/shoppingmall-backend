package ubuthebear.shop.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ubuthebear.shop.global.security.CustomUserDetailsService;

import java.io.IOException;

// JWT 인증을 처리하는 필터 컴포넌트
@Component
// 필수 의존성 자동 주입을 위한 lombok 어노테이션
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    // 모든 요청에 대해 한 번씩 실행되는 필터 메소드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. 요청 헤더에서 JWT 토큰 추출
        String token = getJwtFromRequest(request);

        // 2. 토큰 유효성 검사 (토큰이 유효한 경우 인증 처리)
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            // 3. 토큰에서 사용자 정보 (username) 추출
            String username = tokenProvider.getUsernameFromToken(token);
            // 4. 사용자 정보 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5. 인증 정보 (인증 객체) 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // 6. SecurityContext에 인증 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 7. 다음 필터로 전달
        chain.doFilter(request, response);
    }

    // HTTP 요청의 Authorization 헤더에서 JWT 토큰 추출
    // HTTP 요청의 헤더에는 다양한 정보가 포함되며,
    // JWT는 일반적으로 Authorization 헤더에 "Bearer {토큰값}" 형식으로 전송
    // 이 헤더에서 "Bearer " 다음에 오는 실제 토큰 값을 추출함
    private String getJwtFromRequest(HttpServletRequest request) {
        // "Bearer {token}" 형식에서 토큰 부분만 추출
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
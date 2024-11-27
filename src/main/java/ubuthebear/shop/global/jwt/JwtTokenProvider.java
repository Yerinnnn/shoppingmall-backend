package ubuthebear.shop.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ubuthebear.shop.global.config.JwtConfig;

import java.security.Key;
import java.util.Date;

// JWT 토큰 생성 및 검증을 담당하는 컴포넌트
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    // 사용자명으로 JWT 토큰 생성
    // 사용자가 로그인에 성공하면, 해당 사용자의 식별자(username)를 포함한 JWT 토큰을 생성함
    // 이 토큰에는 사용자 정보, 발행 시간, 만료 시간 등이 포함됨
    // 클라이언트는 이후의 요청에서 이 토큰을 사용하여 자신을 인증함
    public String generateToken(String username) {
        // Base64로 인코딩된 비밀키를 디코딩
        // JWT는 URL-safe 해야 하므로, 비밀키도 Base64로 인코딩하여 안전하게 전송/저장함
        // Base64는 바이너리 데이터를 텍스트로 안전하게 표현할 수 있게 해줌
        // application.properties에서 설정한 비밀키를 안전하게 다루기 위해 사용함
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        // HMAC-SHA 알고리즘용 키 생성
        // HMAC-SHA : 메시지 인증에 널리 사용되는 암호화 알고리즘
        // 빠른 처리 속도, 높은 보안성, 데이터 무결성 보장
        // JWT에서는 토큰이 변조되지 않았음을 검증하기 위해 사용함
        Key key = Keys.hmacShaKeyFor(keyBytes);

        // JWT 토큰 생성
        return Jwts.builder()
                .setSubject(username)  // 토큰 제목(사용자명)
                .setIssuedAt(new Date())  // 토큰 발행 시간
                // 토큰 만료 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() +
                        1000 * jwtConfig.getTokenValidityInSeconds()))
                // HS512 알고리즘으로 서명
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            // 토큰 파싱 시도 - 성공하면 유효한 토큰
            Jwts.parserBuilder()
                    .setSigningKey(Decoders.BASE64.decode(jwtConfig.getSecret()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 파싱 실패 시 유효하지 않은 토큰
            return false;
        }
    }

    // JWT 토큰에서 사용자명 추출
    public String getUsernameFromToken(String token) {
        // 토큰의 클레임(내용) 파싱
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Decoders.BASE64.decode(jwtConfig.getSecret()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 사용자명 반환
        return claims.getSubject();
    }
}
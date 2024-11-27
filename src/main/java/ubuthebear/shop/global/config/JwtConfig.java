package ubuthebear.shop.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

// application.properties/yml에서 'jwt' 프리픽스로 시작하는 설정값들을 바인딩
@ConfigurationProperties(prefix = "jwt")
// 스프링 컴포넌트로 등록하여 다른 클래스에서 주입받아 사용할 수 있게 함
@Component
@Getter @Setter
public class JwtConfig {
    // JWT 토큰 생성 시 사용할 비밀키
    private String secret;
    // JWT 토큰의 유효 기간 (초 단위)
    private long tokenValidityInSeconds;
}
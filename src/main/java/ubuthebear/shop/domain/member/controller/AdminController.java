package ubuthebear.shop.domain.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /**
     * 관리자 권한 확인 API 엔드포인트
     * 관리자 권한이 있는 사용자만 접근 가능
     * @return 관리자 권한 확인 응답
     */
    @GetMapping("/check-auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> checkAdminAuth() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("isAdmin", true);
        return ResponseEntity.ok(response);
    }
}
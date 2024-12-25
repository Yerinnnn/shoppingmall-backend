package ubuthebear.shop.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ubuthebear.shop.domain.member.dto.PointBalanceResponse;
import ubuthebear.shop.domain.member.dto.PointHistoryResponse;
import ubuthebear.shop.domain.member.service.LoyaltyPointService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 포인트 관리 REST API 컨트롤러
 * 일반 회원의 포인트 조회 및 관리자의 포인트 관리 기능 제공
 */
@Tag(name = "LoyaltyPoint", description = "포인트 관리 API")
@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class LoyaltyPointController {

    private final LoyaltyPointService loyaltyPointService;

    /**
     * 회원의 현재 포인트 잔액을 조회
     * GET /api/points/balance
     *
     * @param authentication 인증된 사용자 정보
     * @return 포인트 잔액 정보
     */
    @Operation(summary = "포인트 잔액 조회", description = "현재 보유중인 포인트 잔액을 조회합니다.")
    @GetMapping("/balance")
    public ResponseEntity<PointBalanceResponse> getPointBalance(Authentication authentication) {
        return ResponseEntity.ok(loyaltyPointService.getPointBalance(authentication.getName()));
    }

    /**
     * 회원의 포인트 변동 이력을 페이징하여 조회
     * GET /api/points/history
     *
     * @param authentication 인증된 사용자 정보
     * @param pageable 페이지 크기, 정렬 등 페이징 정보
     * @return 페이징된 포인트 이력 목록
     */
    @Operation(summary = "포인트 이력 조회", description = "포인트 적립/사용 이력을 조회합니다.")
    @GetMapping("/history")
    public ResponseEntity<Page<PointHistoryResponse>> getPointHistory(
            Authentication authentication,
            Pageable pageable) {
        return ResponseEntity.ok(loyaltyPointService.getPointHistory(authentication.getName(), pageable));
    }

    /**
     * 특정 기간의 포인트 변동 이력을 조회
     * GET /api/points/history/period
     *
     * @param authentication 인증된 사용자 정보
     * @param startDate 조회 시작일시 (ISO 형식)
     * @param endDate 조회 종료일시 (ISO 형식)
     * @param pageable 페이징 정보
     * @return 해당 기간의 포인트 이력 목록
     */
    @Operation(summary = "기간별 포인트 이력 조회", description = "특정 기간의 포인트 적립/사용 이력을 조회합니다.")
    @GetMapping("/history/period")
    public ResponseEntity<Page<PointHistoryResponse>> getPointHistoryByPeriod(
            Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {
        return ResponseEntity.ok(
                loyaltyPointService.getPointHistoryByPeriod(
                        authentication.getName(), startDate, endDate, pageable)
        );
    }

    /**
     * 관리자용 : 회원의 포인트를 수동으로 적립
     * POST /api/points/admin/earn
     *
     * @param username 적립 대상 회원의 아이디
     * @param amount 적립할 포인트 금액
     * @param description 적립 사유
     * @return 적립 후 포인트 잔액 정보
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    @Operation(summary = "포인트 수동 적립", description = "관리자가 회원의 포인트를 수동으로 적립합니다.")
    @PostMapping("/admin/earn")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PointBalanceResponse> adminEarnPoints(
            @RequestParam String username,
            @RequestParam BigDecimal amount,
            @RequestParam String description) {
        return ResponseEntity.ok(
                loyaltyPointService.earnPoints(username, amount, description, null)
        );
    }

    /**
     * 관리자용 : 회원의 포인트를 수동으로 차감
     * POST /api/points/admin/deduct
     *
     * @param username 차감 대상 회원의 아이디
     * @param amount 차감할 포인트 금액
     * @param description 차감 사유
     * @return 차감 후 포인트 잔액 정보
     * @throws RuntimeException 회원을 찾을 수 없거나 잔액이 부족한 경우
     */
    @Operation(summary = "포인트 수동 차감", description = "관리자가 회원의 포인트를 수동으로 차감합니다.")
    @PostMapping("/admin/deduct")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PointBalanceResponse> adminDeductPoints(
            @RequestParam String username,
            @RequestParam BigDecimal amount,
            @RequestParam String description) {
        return ResponseEntity.ok(
                loyaltyPointService.usePoints(username, amount, description, null)
        );
    }

    /**
     * 관리자용 : 회원의 포인트를 수동으로 조정
     * POST /api/points/admin/adjust
     *
     * @param username 조정 대상 회원의 아이디
     * @param amount 조정할 포인트 금액 (양수: 증가, 음수: 감소)
     * @param description 조정 사유
     * @return 조정 후 포인트 잔액 정보
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    @Operation(summary = "포인트 조정", description = "관리자가 회원의 포인트를 조정합니다.")
    @PostMapping("/admin/adjust")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PointBalanceResponse> adminAdjustPoints(
            @RequestParam String username,
            @RequestParam BigDecimal amount,
            @RequestParam String description) {
        return ResponseEntity.ok(
                loyaltyPointService.adjustPoints(username, amount, description)
        );
    }
}
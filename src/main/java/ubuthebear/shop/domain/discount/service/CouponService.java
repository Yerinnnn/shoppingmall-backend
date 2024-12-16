package ubuthebear.shop.domain.discount.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.discount.dto.CouponApplicationResponse;
import ubuthebear.shop.domain.discount.dto.CouponRequest;
import ubuthebear.shop.domain.discount.dto.CouponResponse;
import ubuthebear.shop.domain.discount.entity.Coupon;
import ubuthebear.shop.domain.discount.entity.CouponStatus;
import ubuthebear.shop.domain.discount.entity.Discount;
import ubuthebear.shop.domain.discount.repository.CouponRepository;
import ubuthebear.shop.domain.discount.repository.DiscountRepository;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.repository.MemberRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 쿠폰 관리를 위한 서비스 클래스
 * 쿠폰의 생성, 조회, 적용, 사용 처리 등의 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {
    private final CouponRepository couponRepository;
    private final DiscountRepository discountRepository;
    private final MemberRepository memberRepository;
    private final DiscountService discountService;

    /**
     * 새로운 쿠폰을 생성
     *
     * @param request 쿠폰 생성 요청 정보
     * @return CouponResponse 생성된 쿠폰 정보
     * @throws RuntimeException 할인 정책이나 회원을 찾을 수 없는 경우
     */
    @Transactional
    public CouponResponse createCoupon(CouponRequest request) {
        Discount discount = discountRepository.findById(request.getDiscountId())
                .orElseThrow(() -> new RuntimeException("Discount not found"));

        // String에서 Long으로 변환하거나, CouponRequest의 memberId 타입을 Long으로 변경
        Long memberIdLong = Long.parseLong(request.getMemberId());
        Member member = memberRepository.findById(memberIdLong)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        String code = generateUniqueCouponCode();

        Coupon coupon = new Coupon();
        coupon.setDiscount(discount);
        coupon.setCode(code);
        coupon.setMember(member);

        return new CouponResponse(couponRepository.save(coupon));
    }

    /**
     * 고유한 쿠폰 코드를 생성
     * UUID를 기반으로 8자리 코드를 생성하며, 중복 검사를 수행
     *
     * @return String 생성된 고유 쿠폰 코드
     */
    private String generateUniqueCouponCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (couponRepository.existsByCode(code));
        return code;
    }

    /**
     * 특정 회원의 사용 가능한 쿠폰 목록을 조회
     *
     * @param username 조회할 회원의 사용자명
     * @return List<CouponResponse> 사용 가능한 쿠폰 목록
     * @throws RuntimeException 회원을 찾을 수 없는 경우
     */
    public List<CouponResponse> getMemberCoupons(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return couponRepository.findAvailableCoupons(member, LocalDateTime.now()).stream()
                .map(CouponResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 주문에 쿠폰을 적용하여 할인 금액을 계산
     * 쿠폰의 유효성 검사 및 할인 금액 계산을 수행
     *
     * @param username 쿠폰을 사용할 회원의 사용자명
     * @param couponCode 적용할 쿠폰 코드
     * @param orderAmount 주문 금액
     * @return CouponApplicationResponse 쿠폰 적용 결과
     * @throws RuntimeException 회원을 찾을 수 없거나, 쿠폰이 유효하지 않은 경우
     */
    @Transactional
    public CouponApplicationResponse applyCoupon(String username, String couponCode, BigDecimal orderAmount) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Coupon coupon = couponRepository.findByCodeAndStatus(couponCode, CouponStatus.AVAILABLE)
                .orElseThrow(() -> new RuntimeException("Invalid coupon"));

        if (!coupon.getMember().equals(member)) {
            throw new RuntimeException("Coupon belongs to another member");
        }

        Discount discount = coupon.getDiscount();
        if (LocalDateTime.now().isAfter(discount.getEndAt())) {
            throw new RuntimeException("Coupon has expired");
        }

        BigDecimal discountAmount = discountService.calculateDiscount(discount, orderAmount);

        return new CouponApplicationResponse(
                coupon.getCouponId(),
                discountAmount,
                orderAmount.subtract(discountAmount)
        );
    }

    /**
     * 쿠폰을 사용 완료 상태로 변경
     *
     * @param couponId 사용 처리할 쿠폰 ID
     * @throws RuntimeException 쿠폰을 찾을 수 없는 경우
     */
    @Transactional
    public void markCouponAsUsed(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        coupon.setStatus(CouponStatus.USED);
        coupon.setUsedAt(LocalDateTime.now());
    }
}
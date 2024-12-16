package ubuthebear.shop.domain.discount.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ubuthebear.shop.domain.discount.dto.*;
import ubuthebear.shop.domain.discount.entity.*;
import ubuthebear.shop.domain.discount.repository.DiscountRepository;
import ubuthebear.shop.domain.member.entity.Member;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 할인 정책 관리를 위한 서비스 클래스
 * 할인 정책의 생성, 조회, 할인액 계산 등의 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiscountService {
    private final DiscountRepository discountRepository;

    /**
     * 새로운 할인 정책을 생성
     *
     * @param request 할인 정책 생성 요청 정보
     * @return DiscountResponse 생성된 할인 정책 정보
     */
    @Transactional
    public DiscountResponse createDiscount(DiscountRequest request) {
        Discount discount = new Discount();
        discount.setName(request.getName());
        discount.setDescription(request.getDescription());
        discount.setType(request.getType());
        discount.setValue(request.getValue());
        discount.setMinimumOrderAmount(request.getMinimumOrderAmount());
        discount.setMaximumDiscountAmount(request.getMaximumDiscountAmount());
        discount.setStartAt(request.getStartAt());
        discount.setEndAt(request.getEndAt());

        return new DiscountResponse(discountRepository.save(discount));
    }

    /**
     * 현재 활성화된 할인 정책 목록을 조회
     *
     * @return List<DiscountResponse> 활성화된 할인 정책 목록
     */
    public List<DiscountResponse> getActiveDiscounts() {
        return discountRepository.findActiveDiscounts(LocalDateTime.now()).stream()
                .map(DiscountResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 할인 정책을 비활성화
     *
     * @param discountId 비활성화할 할인 정책 ID
     * @throws RuntimeException 할인 정책을 찾을 수 없는 경우
     */
    @Transactional
    public void deactivateDiscount(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new RuntimeException("Discount not found"));
        discount.setActive(false);
    }

    /**
     * 주문 금액에 대한 할인액을 계산
     * 할인 유형(정률/정액)과 최소 주문금액, 최대 할인금액 조건을 고려하여 계산
     *
     * @param discount 적용할 할인 정책
     * @param orderAmount 주문 금액
     * @return BigDecimal 계산된 할인 금액
     */
    public BigDecimal calculateDiscount(Discount discount, BigDecimal orderAmount) {
        if (orderAmount.compareTo(discount.getMinimumOrderAmount()) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discountAmount;
        if (discount.getType() == DiscountType.PERCENTAGE) {
            discountAmount = orderAmount.multiply(discount.getValue())
                    .divide(new BigDecimal("100"));
        } else {
            discountAmount = discount.getValue();
        }

        if (discount.getMaximumDiscountAmount() != null) {
            discountAmount = discountAmount.min(discount.getMaximumDiscountAmount());
        }

        return discountAmount;
    }
}
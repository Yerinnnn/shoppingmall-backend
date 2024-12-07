package ubuthebear.shop.domain.payment.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ubuthebear.shop.domain.member.entity.Member;
import ubuthebear.shop.domain.member.repository.MemberRepository;
import ubuthebear.shop.domain.order.entity.Order;
import ubuthebear.shop.domain.order.entity.OrderStatus;
import ubuthebear.shop.domain.order.repository.OrderRepository;
import ubuthebear.shop.domain.payment.dto.PaymentRequest;
import ubuthebear.shop.domain.payment.dto.PaymentResponse;
import ubuthebear.shop.domain.payment.entity.Payment;
import ubuthebear.shop.domain.payment.entity.PaymentStatus;
import ubuthebear.shop.domain.payment.repository.PaymentRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 결제 관리를 위한 서비스 클래스
 * 결제 처리, 환불, 조회 등 결제와 관련된 비즈니스 로직을 처리
 *
 * @author ubuthebear
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    /**
     * 결제를 처리
     * 외부 PG사 연동을 통한 실제 결제 처리를 포함
     *
     * @param username 결제자의 사용자명
     * @param request 결제 처리 요청 정보
     * @return PaymentResponse 처리된 결제 정보
     * @throws RuntimeException 주문을 찾을 수 없거나, 권한이 없거나, 결제 처리에 실패한 경우
     */
    @Transactional
    public PaymentResponse processPayment(String username, PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Invalid order status for payment");
        }

        // 결제 처리 로직
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.PROCESSING);
        payment.setTransactionId("TXN" + System.currentTimeMillis());

        try {
            // 외부 결제 시스템 연동 로직이 여기에 구현됨
            // 실제로는 PG사 API를 호출하여 결제 처리
            // 이 예제에서는 항상 성공하는 것으로 가정

            payment.setStatus(PaymentStatus.COMPLETED);
            order.setStatus(OrderStatus.PAID);

            Payment savedPayment = paymentRepository.save(payment);
            orderRepository.save(order);

            return new PaymentResponse(savedPayment);
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.PENDING);
            paymentRepository.save(payment);
            throw new RuntimeException("Payment processing failed", e);
        }
    }

    @Transactional
    public PaymentResponse refundPayment(String username, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        Payment payment = order.getPayment();
        if (payment == null || payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("No completed payment found for this order");
        }

        try {
            // 외부 결제 시스템 환불 처리 로직이 여기에 구현됨
            // 실제로는 PG사 API를 호출하여 환불 처리

            payment.setStatus(PaymentStatus.REFUNDED);
            order.setStatus(OrderStatus.CANCELLED);

            Payment savedPayment = paymentRepository.save(payment);
            orderRepository.save(order);

            return new PaymentResponse(savedPayment);
        } catch (Exception e) {
            throw new RuntimeException("Refund processing failed", e);
        }
    }

    public List<PaymentResponse> getPaymentHistory(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return paymentRepository.findByOrderMember(member).stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentDetails(String username, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getOrder().getMember().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized access");
        }

        return new PaymentResponse(payment);
    }
}
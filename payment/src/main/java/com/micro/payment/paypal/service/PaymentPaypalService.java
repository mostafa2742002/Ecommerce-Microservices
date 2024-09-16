package com.micro.payment.paypal.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.micro.payment.entity.User;
import com.micro.payment.paypal.entity.PaypalOrder;
import com.micro.payment.paypal.entity.UserPayment;
import com.micro.payment.paypal.repo.OrderRepository;
import com.micro.payment.paypal.repo.UserPaymentRepository;
import com.micro.payment.repo.UserRepository;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentPaypalService {

    private PaypalService paypalService;
    private UserRepository userRepository;
    private UserPaymentRepository userPaymentRepository;
    private OrderRepository orderRepository; // For saving orders

    public ResponseEntity<String> createPaymentIntent(Integer userId, Integer amount) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserPayment existingPayment = userPaymentRepository.findByUserId(userId);
        if (existingPayment != null) {
            userPaymentRepository.delete(existingPayment);
        }

        amount = Math.max(amount, 0);

        PaypalOrder order = new PaypalOrder(amount, "USD", "paypal", "sale", "Buy Products");

        String approvalUrl;
        String paymentId;
        try {
            Payment payment = paypalService.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
                    order.getIntent(), order.getDescription(),
                    "http://localhost:8080/payment/cancel",
                    "http://localhost:8080/payment/success");

            paymentId = payment.getId();
            approvalUrl = payment.getLinks().stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Approval URL not found"))
                    .getHref();
        } catch (PayPalRESTException e) {
            throw new RuntimeException("Error creating PayPal payment", e);
        }

        UserPayment userPayment = new UserPayment();
        userPayment.setUserId(userId);
        userPayment.setPaymentId(paymentId);
        userPayment.setApprovalUrl(approvalUrl);
        userPaymentRepository.save(userPayment);

        return ResponseEntity.status(HttpStatus.OK)
                .body(approvalUrl);
    }

    public ResponseEntity<String> handleSuccess(String paymentId, String payerId) throws PayPalRESTException {
        // try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            // return ResponseEntity.status(HttpStatus.OK).body("Payment successful");
            // if ("approved".equals(payment.getState())) {
            //     UserPayment userPayment = userPaymentRepository.findByPaymentId(paymentId);
            //     if (userPayment == null) {
            //         throw new RuntimeException("User payment not found");
            //     }

            //     // Save the order
            //     Order order = new Order();
            //     order.setPrice(Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal()));
            //     order.setCurrency(payment.getTransactions().get(0).getAmount().getCurrency());
            //     order.setMethod(payment.getPayer().getPaymentMethod());
            //     order.setIntent(payment.getIntent());
            //     order.setDescription(payment.getTransactions().get(0).getDescription());
            //     orderRepository.save(order);

            //     // Clear the payment record after successful order processing
            //     userPaymentRepository.delete(userPayment);

                return ResponseEntity.status(HttpStatus.OK).body("Payment successful, order created");
            // }
    //     } catch (PayPalRESTException e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed: " + e.getMessage());
    //     }

    //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment not approved");
    }
}

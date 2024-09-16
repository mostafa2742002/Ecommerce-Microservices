package com.micro.payment.paypal.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.micro.payment.paypal.entity.Delivery;
import com.micro.payment.paypal.entity.PaypalOrder;
import com.micro.payment.paypal.repo.DeliveryRepository;
import com.micro.payment.repo.UserRepository;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.base.rest.PayPalRESTException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentPaypalService {

    private final PaypalService paypalService;
    private final UserRepository userRepository;

    private final DeliveryRepository deliveryRepository;

    public ResponseEntity<String> createPaymentIntent(Integer userId, Integer amount) throws PayPalRESTException {

        if(userRepository.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        amount = Math.max(amount, 0);

        PaypalOrder order = new PaypalOrder(amount, "USD", "paypal", "sale", "Buy Products");

        Payment payment = paypalService.createPayment(
                order.getPrice(),
                order.getCurrency(),
                order.getMethod(),
                order.getIntent(),
                order.getDescription(),
                "http://localhost:8085/payment/cancel",
                "http://localhost:8085/payment/success");

        String approvalUrl = payment.getLinks().stream()
                .filter(link -> link.getRel().equals("approval_url"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Approval URL not found"))
                .getHref();

        return ResponseEntity.ok(approvalUrl);
    }

    public ResponseEntity<String> handleSuccess(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = paypalService.executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {

            ShippingAddress shippingAddress = payment.getPayer().getPayerInfo().getShippingAddress();

            Delivery delivery = new Delivery();
            delivery.setRecipientName(shippingAddress.getRecipientName());
            delivery.setLine1(shippingAddress.getLine1());
            delivery.setCity(shippingAddress.getCity());
            delivery.setCountryCode(shippingAddress.getCountryCode());
            delivery.setPostalCode(shippingAddress.getPostalCode());
            delivery.setState(shippingAddress.getState());
            delivery.setEmail(payment.getPayer().getPayerInfo().getEmail());
            delivery.setStatus("Pending");

            deliveryRepository.save(delivery);

            return ResponseEntity.ok("Payment Success");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment Failed");
    }

}

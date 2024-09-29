package com.micro.payment.paypal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.micro.payment.dto.PaymentContactInfoDto;
import com.micro.payment.entity.PurchaseOrder;
import com.micro.payment.paypal.service.PaymentPaypalService;
import com.paypal.base.rest.PayPalRESTException;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentPaypalController {

    private final PaymentPaypalService paymentPaypalService;
    private final PaymentContactInfoDto paymentContactInfoDto;

    @Retry(name = "retryService", fallbackMethod = "createPaymentIntentFallback")
    @PostMapping("/create")
    public ResponseEntity<String> createPaymentIntent(@RequestParam Integer userId, @RequestParam Integer amount,
            @RequestBody PurchaseOrder order)
            throws PayPalRESTException {
        return paymentPaypalService.createPaymentIntent(userId, amount, order);
    }

    public ResponseEntity<String> createPaymentIntentFallback(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment creation failed");
    }

    @PostMapping("/success")
    public ResponseEntity<String> handleSuccess(@RequestParam String paymentId, @RequestParam String token,
            @RequestParam String payerId) throws PayPalRESTException {
        return paymentPaypalService.handleSuccess(paymentId, payerId);
    }

    @Retry(name = "retryService", fallbackMethod = "handleSuccessFallback")
    @GetMapping("/contact-info")
    public ResponseEntity<PaymentContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paymentContactInfoDto);
    }

    public ResponseEntity<PaymentContactInfoDto> handleSuccessFallback(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PaymentContactInfoDto());
    }
}

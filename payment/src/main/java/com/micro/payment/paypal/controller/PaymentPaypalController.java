package com.micro.payment.paypal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.micro.payment.dto.PaymentContactInfoDto;
import com.micro.payment.paypal.service.PaymentPaypalService;
import com.paypal.base.rest.PayPalRESTException;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentPaypalController {

    
    private final PaymentPaypalService paymentPaypalService;
    private final PaymentContactInfoDto paymentContactInfoDto;

    @PostMapping("/create")
    public ResponseEntity<String> createPaymentIntent(@RequestParam Integer userId, @RequestParam Integer amount)
            throws PayPalRESTException {
        return paymentPaypalService.createPaymentIntent(userId, amount);
    }

    @PostMapping("/success")
    public ResponseEntity<String> handleSuccess(@RequestParam String paymentId, @RequestParam String token,
            @RequestParam String payerId) throws PayPalRESTException {
        return paymentPaypalService.handleSuccess(paymentId, payerId);
    }

    @GetMapping("/contact-info")
    public ResponseEntity<PaymentContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paymentContactInfoDto);
    }

}

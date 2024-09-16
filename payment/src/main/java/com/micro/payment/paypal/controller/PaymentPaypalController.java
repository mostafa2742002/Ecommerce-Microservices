package com.micro.payment.paypal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.micro.payment.paypal.service.PaymentPaypalService;
import com.paypal.base.rest.PayPalRESTException;

@RestController
@RequestMapping("/payment")
public class PaymentPaypalController {

    @Autowired
    private PaymentPaypalService paymentPaypalService;

    @PostMapping("/create")
    public ResponseEntity<String> createPaymentIntent(@RequestParam Integer userId, @RequestParam Integer amount) {
        return paymentPaypalService.createPaymentIntent(userId, amount);
    }

    @PostMapping("/success")
    public ResponseEntity<String> handleSuccess(@RequestParam String paymentId, @RequestParam String payerId) throws PayPalRESTException {
        return paymentPaypalService.handleSuccess(paymentId, payerId);
    }
}

package com.micro.payment.paypal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.micro.payment.paypal.entity.TransactionCallback;
import com.micro.payment.paypal.service.PaymentPaypalService;
import com.micro.payment.paypal.service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaypalController {

    @Autowired
    private PaypalService service;

    @Autowired
    private PaymentPaypalService paymentPaypalService;

    public static final String SUCCESS_URL = "/payment/success";
    public static final String CANCEL_URL = "/payment/cancel";

    @GetMapping(CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) throws PayPalRESTException {
        return paymentPaypalService.handleSuccess(paymentId, payerId).getBody();
    }
}

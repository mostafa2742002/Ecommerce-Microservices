package com.micro.payment.paypal.entity;

import lombok.Data;

@Data
public class TransactionCallback {

    private TransactionObject obj;

    @Data
    public static class TransactionObject {
        private boolean success;
        private CallbackOrder order;
        private int amountCents;
    }

    @Data
    public static class CallbackOrder {
        private ShippingData shippingData;
        private String paymentId;

    }

    @Data
    public static class ShippingData {
        private String email;

    }
}
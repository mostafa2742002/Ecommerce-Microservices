package com.micro.payment.paypal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "paypal_order")
public class PaypalOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double price;
    private String currency;
    private String method;
    private String intent;
    private String description;

    // example (amount, "USD", "paypal", "sale", "Buy Products")
    public PaypalOrder(Integer amount, String currency, String method, String intent, String description) {
        this.price = amount;
        this.currency = currency;
        this.method = method;
        this.intent = intent;
        this.description = description;

    }
}
package com.micro.payment.paypal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String recipientName;
    private String line1;
    private String city;
    private String countryCode;
    private String postalCode;
    private String state;
    private String email;
    private String status; // e.g., "Pending", "Shipped", "Delivered", etc.
}

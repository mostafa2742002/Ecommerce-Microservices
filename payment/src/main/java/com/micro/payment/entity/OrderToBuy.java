package com.micro.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "order_to_buy")
public class OrderToBuy {
    private String orderId;
    private Integer userId;
    private Order order;
}

package com.micro.gatewayservice.controller;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class FallbackController {

    public String cartServiceFallback(){
        return "Cart Service is taking too long to respond or is down. Please try again later";
    }

    public String inventoryServiceFallback(){
        return "Inventory Service is taking too long to respond or is down. Please try again later";
    }

    public String orderServiceFallback(){
        return "Order Service is taking too long to respond or is down. Please try again later";
    }

    public String paymentServiceFallback(){
        return "Payment Service is taking too long to respond or is down. Please try again later";
    }

    public String productServiceFallback(){
        return "Product Service is taking too long to respond or is down. Please try again later";
    }

    public String userServiceFallback(){
        return "User Service is taking too long to respond or is down. Please try again later";
    }
    
}

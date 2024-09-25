package com.example.cartservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "inventoryservice")
public interface Inventoryfeignclient {

    @GetMapping(value = "/api/inventory/{productId}/check?quantity={quantity}", consumes = "application/json")
    public Boolean checkInventory(Integer productId, Integer quantity);
}

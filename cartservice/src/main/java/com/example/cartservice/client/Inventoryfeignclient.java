package com.example.cartservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "INVENTORYSERVICE")
public interface Inventoryfeignclient {

    @GetMapping(value = "/api/inventory/{productId}/check")
    Boolean checkInventory(@PathVariable("productId") Integer productId,@RequestParam("quantity") Integer quantity);
}

package com.example.cartservice.client;

import org.springframework.stereotype.Component;

@Component
public class InventoryFallback implements Inventoryfeignclient {

    @Override
    public Boolean checkInventory(Integer productId, Integer quantity) {
        return false;
    }
}

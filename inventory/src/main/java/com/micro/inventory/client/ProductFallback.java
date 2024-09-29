package com.micro.inventory.client;

import org.springframework.stereotype.Component;

@Component
public class ProductFallback implements ProductFeignClient {

    @Override
    public Boolean checkProduct(Integer id) {
        return false;
    }

}

package com.micro.inventory.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "PRODUCTSERVICE", fallback = ProductFallback.class)
public interface ProductFeignClient {

    @GetMapping(value = "/api/product/{id}/check", consumes = "application/json")
    public Boolean checkProduct(Integer id);

}

package com.micro.inventory.dto;

import com.micro.inventory.entity.Product;

import lombok.Data;

@Data
public class InventoryDTO {

    private Integer productId;
    private Long stockQuantity;

    public InventoryDTO() {}

    public InventoryDTO(Integer productId, Long stockQuantity) {
        this.productId = productId;
        this.stockQuantity = stockQuantity;
    }

    public Product getProduct() {
        Product product = new Product();
        product.setId(this.productId);
        return product;
    }
}

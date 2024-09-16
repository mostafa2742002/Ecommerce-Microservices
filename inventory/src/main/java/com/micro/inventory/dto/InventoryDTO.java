package com.micro.inventory.dto;

import com.micro.inventory.entity.Product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryDTO {

    @NotNull(message = "Product ID cannot be null")
    private Integer productId;
    @NotNull(message = "Stock quantity cannot be null")
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

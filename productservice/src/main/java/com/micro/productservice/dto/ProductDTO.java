package com.micro.productservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {

    @NotNull(message = "Product name cannot be null")
    private String name;

    @NotNull(message = "Product description cannot be null")
    private String description;

    @NotNull(message = "Product price cannot be null")
    private BigDecimal price;

    @NotNull(message = "Product category cannot be null")
    private Integer categoryId;
}

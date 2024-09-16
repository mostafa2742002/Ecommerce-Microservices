package com.example.cartservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDTO {

    @NotNull(message = "Category name cannot be null")
    private String name;

    @NotNull(message = "Category description cannot be null")
    private String description;
}

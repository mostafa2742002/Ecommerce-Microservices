package com.micro.productservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.productservice.dto.ProductContactInfoDto;
import com.micro.productservice.dto.ProductDTO;
import com.micro.productservice.entity.Product;
import com.micro.productservice.exceptions.ErrorResponseDto;
import com.micro.productservice.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

        private final ProductService productService;
        private final ProductContactInfoDto ProductContactInfoDto;

        @Operation(summary = "Create a new product", description = "Create a new product based on the provided data")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input")
        })
        @PostMapping
        public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductDTO product) {
                Product createdProduct = productService.createProduct(product);
                return ResponseEntity.ok(createdProduct);
        }

        @Operation(summary = "Get all products", description = "Retrieve a list of all products")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)))
        })
        @GetMapping
        public ResponseEntity<List<Product>> getAllProducts() {
                List<Product> products = productService.getAllProducts();
                return ResponseEntity.ok(products);
        }

        @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        @GetMapping("/{id}")
        public ResponseEntity<Product> getProductById(@PathVariable @NotNull Integer id) {
                Product product = productService.getProductById(id);
                return ResponseEntity.ok(product);
        }

        @Operation(summary = "Update product by ID", description = "Update an existing product by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
                        @ApiResponse(responseCode = "404", description = "Product not found"),
                        @ApiResponse(responseCode = "400", description = "Invalid input")
        })
        @PutMapping("/{id}")
        public ResponseEntity<Product> updateProduct(@PathVariable @NotNull Integer id,
                        @RequestBody @Valid ProductDTO updatedProduct) {
                Product updated = productService.updateProduct(id, updatedProduct);
                return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
        }

        @Operation(summary = "Delete product by ID", description = "Delete a specific product by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "Product not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteProduct(@PathVariable @NotNull Integer id) {
                productService.deleteProduct(id);
                return ResponseEntity.noContent().build();
        }

        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        @GetMapping("/contact-info")
        public ResponseEntity<ProductContactInfoDto> getContactInfo() {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(ProductContactInfoDto);
        }

        @GetMapping(value = "/product/{id}/check")
        public Boolean checkProduct(@PathVariable Integer id) {
                return productService.checkProduct(id);
        }
}

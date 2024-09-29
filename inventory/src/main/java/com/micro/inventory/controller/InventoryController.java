package com.micro.inventory.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.micro.inventory.dto.InventoryContactInfoDto;
import com.micro.inventory.dto.InventoryDTO;
import com.micro.inventory.entity.Inventory;
import com.micro.inventory.entity.Product;
import com.micro.inventory.exceptions.ErrorResponseDto;
import com.micro.inventory.service.InventoryService;

import io.github.resilience4j.retry.annotation.Retry;
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
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryContactInfoDto inventoryContactInfoDto;

    @Operation(summary = "Create or update inventory", description = "Create a new inventory record or update an existing one for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory created/updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inventory.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/create")
    public ResponseEntity<Inventory> createInventory(@RequestBody @Valid InventoryDTO inventoryDTO) {
        Product product = inventoryDTO.getProduct();
        Long stockQuantity = inventoryDTO.getStockQuantity();
        Inventory inventory = inventoryService.createOrUpdateInventory(product, stockQuantity);
        return new ResponseEntity<>(inventory, HttpStatus.CREATED);
    }

    @Operation(summary = "Buy product", description = "Reduce stock quantity of a product after a purchase")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product purchase recorded successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock")
    })
    @PostMapping("/buy")
    public ResponseEntity<Void> buyProduct(@RequestBody @Valid InventoryDTO inventoryDTO) {
        Product product = inventoryDTO.getProduct();
        Long quantity = inventoryDTO.getStockQuantity();
        inventoryService.buyProduct(product, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get inventory by product ID", description = "Retrieve inventory details for a specific product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inventory.class))),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    @Retry(name = "retryService", fallbackMethod = "getInventoryFallback")
    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getInventory(@PathVariable @NotNull Integer productId) {
        Product product = new Product();
        product.setId(productId);
        Optional<Inventory> inventory = inventoryService.getInventoryByProduct(product);
        return inventory.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Inventory> getInventoryFallback(Integer productId, Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Get all inventories", description = "Retrieve a list of all inventories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventories retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Inventory.class)))
    })
    @Retry(name = "retryService", fallbackMethod = "getAllInventoriesFallback")
    @GetMapping("/all")
    public ResponseEntity<List<Inventory>> getAllInventories() {
        List<Inventory> inventories = inventoryService.getAllInventories();
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }

    public ResponseEntity<List<Inventory>> getAllInventoriesFallback(Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Operation(summary = "Delete inventory", description = "Delete an inventory record by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inventory deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Inventory not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable @NotNull Integer id) {
        inventoryService.deleteInventory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get Contact Info", description = "Contact Info details that can be reached out in case of any issues")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @Retry(name = "retryService", fallbackMethod = "getContactInfoFallback")
    @GetMapping("/contact-info")
    public ResponseEntity<InventoryContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inventoryContactInfoDto);
    }

    public ResponseEntity<InventoryContactInfoDto> getContactInfoFallback(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InventoryContactInfoDto());
    }

    @Operation(summary = "Check inventory", description = "Check if there is enough stock for a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory check successful"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock")
    })
    @Retry(name = "retryService", fallbackMethod = "checkInventoryFallback")
    @GetMapping("/{productId}/check")
    public Boolean checkInventory(@PathVariable Integer productId, @RequestParam Integer quantity) {
        return inventoryService.checkInventory(productId, quantity);
    }

    public Boolean checkInventoryFallback(Integer productId, Integer quantity, Exception e) {
        return false;
    }
}

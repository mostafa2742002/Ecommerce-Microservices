package com.example.cartservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.cartservice.entity.Cart;
import com.example.cartservice.entity.CartItem;
import com.example.cartservice.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Create a new cart for a user", description = "Create a new cart for a specific user based on user ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cart created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/user/{userId}")
    public ResponseEntity<Cart> createCart(@PathVariable Integer userId) {
        Cart cart = cartService.createCart(userId);
        return ResponseEntity.status(201).body(cart);
    }

    @Operation(summary = "Get cart by ID", description = "Retrieve the cart and its items by the cart ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCartById(@PathVariable Integer cartId) {
        Cart cart = cartService.getCartById(cartId);
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Add item to cart", description = "Add a new item to the specified cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item added to cart successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cart.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{cartId}/items")
    public ResponseEntity<Cart> addItemToCart(@PathVariable Integer cartId, @RequestBody CartItem item) {
        Cart updatedCart = cartService.addItemToCart(cartId, item);
        return ResponseEntity.ok(updatedCart);
    }

    @Operation(summary = "Remove item from cart", description = "Remove a specific item from the cart by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item removed from cart successfully"),
        @ApiResponse(responseCode = "404", description = "Cart or item not found")
    })
    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Integer cartId, @PathVariable Integer itemId) {
        cartService.removeItemFromCart(cartId, itemId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Clear the cart", description = "Remove all items from the cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart cleared successfully"),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all items in the cart", description = "Retrieve all items in a specific cart")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Items retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartItem.class))),
        @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItem>> getAllItemsInCart(@PathVariable Integer cartId) {
        List<CartItem> items = cartService.getAllItemsInCart(cartId);
        return ResponseEntity.ok(items);
    }
}

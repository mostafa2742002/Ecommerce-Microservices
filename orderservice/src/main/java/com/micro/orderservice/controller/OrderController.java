package com.micro.orderservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.micro.orderservice.dto.OrderContactInfoDto;
import com.micro.orderservice.entity.Order;
import com.micro.orderservice.entity.User;
import com.micro.orderservice.exceptions.ErrorResponseDto;
import com.micro.orderservice.service.OrderService;

import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

        private final OrderService orderService;
        private final OrderContactInfoDto orderContactInfoDto;

        @Operation(summary = "Place an order", description = "Create a new order based on the cart ID and user ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order placed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "404", description = "User or cart not found")
        })
        @PostMapping("/place")
        public ResponseEntity<Order> placeOrder(
                        @RequestParam Integer cartId,
                        @RequestParam Integer userId) {
                Order order = orderService.placeOrder(cartId, userId);
                return ResponseEntity.ok(order);
        }

        @Operation(summary = "Get orders by user", description = "Retrieve all orders for a specific user by user ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Orders retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        @Retry(name = "retryService", fallbackMethod = "getOrdersByUserFallback")
        @GetMapping("/user/{userId}")
        public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Integer userId) {
                User user = new User();
                user.setUserId(userId);
                List<Order> orders = orderService.getOrdersByUser(user);
                return ResponseEntity.ok(orders);
        }

        public ResponseEntity<List<Order>> getOrdersByUserFallback(Integer userId, Exception e) {
                return ResponseEntity.ok(List.of());
        }

        @Operation(summary = "Update order status", description = "Update the status of a specific order by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order status updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                        @ApiResponse(responseCode = "404", description = "Order not found"),
                        @ApiResponse(responseCode = "400", description = "Invalid status or order ID")
        })
        @PutMapping("/{orderId}/status")
        public ResponseEntity<Order> updateOrderStatus(
                        @PathVariable Integer orderId,
                        @RequestParam String status) {
                Order updatedOrder = orderService.updateOrderStatus(orderId, status);
                return ResponseEntity.ok(updatedOrder);
        }

        @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Order retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                        @ApiResponse(responseCode = "404", description = "Order not found")
        })
        @Retry(name = "retryService", fallbackMethod = "getOrderByIdFallback")
        @GetMapping("/{orderId}")
        public ResponseEntity<Order> getOrderById(@PathVariable Integer orderId) {
                Order order = orderService.getOrderById(orderId);
                return ResponseEntity.ok(order);
        }

        public ResponseEntity<Order> getOrderByIdFallback(Integer orderId, Exception e) {
                return ResponseEntity.ok(new Order());
        }

        @Operation(summary = "Get Contact Info", description = "Contact Info details that can be reached out in case of any issues")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
                        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
        })
        @Retry(name = "retryService", fallbackMethod = "getContactInfoFallback")
        @GetMapping("/contact-info")
        public ResponseEntity<OrderContactInfoDto> getContactInfo() {
                return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(orderContactInfoDto);
        }

        public ResponseEntity<OrderContactInfoDto> getContactInfoFallback(Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderContactInfoDto());
        }
}

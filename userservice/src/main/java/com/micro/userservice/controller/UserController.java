package com.micro.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.micro.userservice.dto.UserContactInfoDto;
import com.micro.userservice.dto.UserDTO;
import com.micro.userservice.entity.User;
import com.micro.userservice.exceptions.ErrorResponseDto;
import com.micro.userservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserContactInfoDto userContactInfoDto;

    @Operation(summary = "Sign up a new user", description = "Creates a new user in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "User with the given email already exists", content = @Content)
    })
    @PostMapping("/signup")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserDTO newUser) {
        userService.addUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User added successfully");
    }

    @Operation(summary = "Retrieve a user by ID", description = "Fetches a user from the system by userId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable @NotNull Integer userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @Operation(summary = "Get all users", description = "Fetches all users in the system.")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Update user details", description = "Updates an existing user's details in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO user) {
        userService.updateUser(user);
        return ResponseEntity.ok("User updated successfully");
    }

        @Operation(
            summary = "Get Contact Info",
            description = "Contact Info details that can be reached out in case of any issues"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/contact-info")
    public ResponseEntity<UserContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userContactInfoDto);
    }



}

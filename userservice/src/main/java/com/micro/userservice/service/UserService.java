package com.micro.userservice.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.micro.userservice.dto.UserDTO;
import com.micro.userservice.entity.User;
import com.micro.userservice.exceptions.ResourceNotFoundException;
import com.micro.userservice.exceptions.UserAlreadyExistsException;
import com.micro.userservice.repo.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    public void addUser(@Valid @NotNull UserDTO newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + newUser.getEmail() + " already exists");
        }

        User user = new User(newUser);
        userRepository.save(user);
    }

    public User getUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The User", "UserId", userId));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUser(UserDTO user) {
        User existingUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("The User", "UserId", user.getUserId()));

        if (user.getUserName() != null) {
            existingUser.setUserName(user.getUserName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        }
        userRepository.save(existingUser);
    }
}

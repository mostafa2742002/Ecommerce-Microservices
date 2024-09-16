package com.example.cartservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cartservice.entity.Cart;
import com.example.cartservice.entity.CartItem;
import com.example.cartservice.entity.User;
import com.example.cartservice.exceptions.ResourceNotFoundException;
import com.example.cartservice.repo.CartRepository;
import com.example.cartservice.repo.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartService {

    private CartRepository cartRepository;
    private UserRepository userRepository;

    public Cart createCart(Integer userId) {
        Cart cart = new Cart();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        user.getCarts().add(cart);

        cart.setUser(user);

        User savedUser = userRepository.save(user);

        return savedUser.getCarts().get(savedUser.getCarts().size() - 1);
    }

    public void clearCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    public Cart getCartById(Integer cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
    }

    public Cart addItemToCart(Integer cartId, CartItem item) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        cart.getItems().add(item);
        updateTotalPrice(cart);
        return cartRepository.save(cart);
    }

    public void removeItemFromCart(Integer cartId, Integer itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        updateTotalPrice(cart);
        cartRepository.save(cart);
    }

    private void updateTotalPrice(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }

    public List<CartItem> getAllItemsInCart(Integer cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));
        return cart.getItems();
    }
}

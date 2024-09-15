package com.example.cartservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cartservice.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> { }

package com.micro.orderservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.orderservice.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> { }

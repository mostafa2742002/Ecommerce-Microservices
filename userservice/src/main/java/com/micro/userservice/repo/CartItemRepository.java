package com.micro.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.userservice.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> { }

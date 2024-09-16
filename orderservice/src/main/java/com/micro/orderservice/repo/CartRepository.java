package com.micro.orderservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.orderservice.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}
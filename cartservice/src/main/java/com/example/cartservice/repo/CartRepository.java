package com.example.cartservice.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cartservice.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}

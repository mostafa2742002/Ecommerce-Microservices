package com.micro.auth.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.auth.server.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}

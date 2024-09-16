package com.micro.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.userservice.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

}

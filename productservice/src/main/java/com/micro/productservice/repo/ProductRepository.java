package com.micro.productservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}

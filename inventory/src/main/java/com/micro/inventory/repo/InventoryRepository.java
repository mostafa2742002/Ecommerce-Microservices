package com.micro.inventory.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.inventory.entity.Inventory;
import com.micro.inventory.entity.Product;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findByProduct(Product product);

    Optional<Inventory> findByProductId(Integer productId);
}

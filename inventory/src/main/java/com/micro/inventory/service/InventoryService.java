package com.micro.inventory.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.inventory.entity.Inventory;
import com.micro.inventory.entity.Product;
import com.micro.inventory.repo.InventoryRepository;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    public Inventory createOrUpdateInventory(Product product, Long stockQuantity) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProduct(product);

        Inventory inventory;
        if (inventoryOpt.isPresent()) {
            inventory = inventoryOpt.get();
            inventory.setStockQuantity(stockQuantity);
        } else {
            inventory = new Inventory(product, stockQuantity);
        }
        return inventoryRepository.save(inventory);
    }

    public void buyProduct(Product product, Long quantity) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProduct(product);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            Long stockQuantity = inventory.getStockQuantity();
            if (stockQuantity >= quantity) {
                inventory.setStockQuantity(stockQuantity - quantity);
                inventoryRepository.save(inventory);
            } else {
                throw new RuntimeException("Not enough stock available");
            }
        } else {
            throw new RuntimeException("Product not found in inventory");
        }
    }

    public Optional<Inventory> getInventoryByProduct(Product product) {
        return inventoryRepository.findByProduct(product);
    }

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    public void deleteInventory(Integer id) {
        inventoryRepository.deleteById(id);
    }
}

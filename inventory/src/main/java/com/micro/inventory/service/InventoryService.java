package com.micro.inventory.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.inventory.client.ProductFeignClient;
import com.micro.inventory.entity.Inventory;
import com.micro.inventory.entity.Product;
import com.micro.inventory.repo.InventoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InventoryService {

    private InventoryRepository inventoryRepository;
    private ProductFeignClient productFeignClient;

    public Inventory createOrUpdateInventory(Product product, Long stockQuantity) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProduct(product);

        Boolean isProductFound = productFeignClient.checkProduct(product.getId());
        if (!isProductFound) {
            throw new RuntimeException("Product not found");
        }

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

    public Boolean checkInventory(Integer productId, Integer quantity) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            return inventory.getStockQuantity() >= quantity;
        }
        return false;
    }
}

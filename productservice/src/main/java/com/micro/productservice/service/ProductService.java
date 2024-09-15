package com.micro.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.productservice.dto.ProductDTO;
import com.micro.productservice.entity.Product;
import com.micro.productservice.exceptions.ResourceNotFoundException;
import com.micro.productservice.repo.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(ProductDTO product) {
        Product newProduct = new Product(product);
        return productRepository.save(newProduct);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Producr Id", id));
    }

    public Product updateProduct(Long id, ProductDTO updatedProduct) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "Product Id", id);
        }

        Product product = new Product(updatedProduct);
        product.setId(id);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "Product Id", id);
        }

        productRepository.deleteById(id);
    }
}

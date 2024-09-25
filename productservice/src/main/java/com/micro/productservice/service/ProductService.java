package com.micro.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micro.productservice.dto.ProductDTO;
import com.micro.productservice.entity.Category;
import com.micro.productservice.entity.Product;
import com.micro.productservice.exceptions.ResourceNotFoundException;
import com.micro.productservice.repo.CategoryRepository;
import com.micro.productservice.repo.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Product createProduct(ProductDTO product) {

        Category category = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", product.getCategoryId()));

        Product newProduct = new Product(product, category);
        return productRepository.save(newProduct);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Producr Id", id));
    }

    public Product updateProduct(Integer id, ProductDTO updatedProduct) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "Product Id", id);
        }

        Category category = categoryRepository.findById(updatedProduct.getCategoryId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category", "Category Id", updatedProduct.getCategoryId()));

        Product product = new Product(updatedProduct, category);
        product.setId(id);

        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "Product Id", id);
        }

        productRepository.deleteById(id);
    }

    public Boolean checkProduct(Integer id) {
        return productRepository.existsById(id);
    }
}

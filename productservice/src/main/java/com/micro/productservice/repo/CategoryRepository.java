package com.micro.productservice.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.productservice.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

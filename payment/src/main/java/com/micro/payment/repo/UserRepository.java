package com.micro.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.payment.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}

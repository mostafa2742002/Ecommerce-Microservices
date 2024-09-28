package com.micro.payment.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.payment.entity.PurchaseOrder;
import com.micro.payment.entity.User;



@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    List<PurchaseOrder> findByUser(User user);
}

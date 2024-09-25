package com.micro.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.payment.entity.OrderToBuy;

@Repository
public interface OrderToBuyRepository extends JpaRepository<OrderToBuy, Long> {

    OrderToBuy findByOrderId(String paymentId);

}

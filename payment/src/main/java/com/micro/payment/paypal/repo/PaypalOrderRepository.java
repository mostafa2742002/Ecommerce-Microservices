package com.micro.payment.paypal.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.payment.paypal.entity.PaypalOrder;

@Repository
public interface PaypalOrderRepository extends JpaRepository<PaypalOrder, Long> {
}

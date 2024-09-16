package com.micro.payment.paypal.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.micro.payment.paypal.entity.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}

package com.micro.payment.paypal.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.micro.payment.paypal.entity.UserPayment;

@Repository
public interface UserPaymentRepository extends JpaRepository<UserPayment, Integer> {
    UserPayment findByPaymentId(String paymentId);

    UserPayment findByUserId(Integer userId);
}

package com.yash.trading.repository;

import com.yash.trading.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsRepository extends JpaRepository <PaymentDetails, Long> {

    PaymentDetails findByUserId( Long userId);
}

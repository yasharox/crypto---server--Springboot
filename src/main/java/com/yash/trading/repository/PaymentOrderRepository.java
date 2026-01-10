package com.yash.trading.repository;

import com.yash.trading.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository <PaymentOrder, Long> {


}

package com.yash.trading.repository;

import com.yash.trading.model.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;



// TwoFactorOtpRepository.java
public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {
    // Add this method definition
    TwoFactorOTP findByUserId(Long userId);
}




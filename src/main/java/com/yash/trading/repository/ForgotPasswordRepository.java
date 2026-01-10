package com.yash.trading.repository;

import com.yash.trading.model.ForgetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository <ForgetPasswordToken, String> {


    ForgetPasswordToken findByuserId ( Long userId);
}

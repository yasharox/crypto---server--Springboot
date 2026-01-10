package com.yash.trading.service;

import com.yash.trading.domain.VerificationType;
import com.yash.trading.model.ForgetPasswordToken;
import com.yash.trading.model.User;

public interface ForgotPasswordService {

    ForgetPasswordToken createToken(User user,
                                    String id, String otp,
                                    VerificationType verificationType,
                                    String sendTo);

    ForgetPasswordToken findById (String id);

    ForgetPasswordToken findByUser (Long userId);

    void deleteToken (ForgetPasswordToken token);



}

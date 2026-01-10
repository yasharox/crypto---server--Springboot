package com.yash.trading.service;

import com.yash.trading.model.TwoFactorOTP;
import com.yash.trading.model.User;

public interface TwoFactorOtpService {

    TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);

    TwoFactorOTP findByUserId(Long UserId);

    TwoFactorOTP findByUser(Long UserId);

    TwoFactorOTP findById(String Id);



    boolean VerifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOtp (TwoFactorOTP twoFactorOTP);

}

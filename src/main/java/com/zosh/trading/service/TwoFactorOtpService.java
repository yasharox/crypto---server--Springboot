package com.zosh.trading.service;

import com.zosh.trading.model.TwoFactorOTP;
import com.zosh.trading.model.User;

public interface TwoFactorOtpService {

    TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);

    TwoFactorOTP findByUserId(Long UserId);

    TwoFactorOTP findByUser(Long UserId);

    TwoFactorOTP findById(String Id);

    boolean VerifyTwoFactotrOtp (TwoFactorOTP twoFactorOTP, String otp);

    boolean VerifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp);

    void deleteTwoFactorOtp (TwoFactorOTP twoFactorOTP);

}

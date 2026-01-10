package com.yash.trading.service;

import com.yash.trading.domain.VerificationType;
import com.yash.trading.model.User;
import com.yash.trading.model.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById (Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId );

    void deleteVerificationCodeById (VerificationCode verificationCode);

}

package com.yash.trading.request;

import com.yash.trading.domain.VerificationType;
import lombok.Data;

@Data

public class ForgotPasswordTokenRequest {

    private String sendTo;
    private VerificationType verificationType;
}

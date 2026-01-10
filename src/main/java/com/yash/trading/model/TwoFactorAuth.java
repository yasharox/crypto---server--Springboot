package com.yash.trading.model;

import com.yash.trading.domain.VerificationType;
import lombok.Data;

@Data

public class TwoFactorAuth {

    private boolean isEnabled = false;

    private VerificationType sendTo;
}

package com.zosh.trading.model;

import com.zosh.trading.domain.VerificationType;
import lombok.Data;

@Data

public class TwoFactorAuth {

    private boolean isEnabled = false;

    private VerificationType sendTo;
}

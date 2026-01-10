package com.yash.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class TwoFactorOTP {

    @Id
    private String Id;

    private String otp;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @OneToOne
    private User user;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String jwt;
}

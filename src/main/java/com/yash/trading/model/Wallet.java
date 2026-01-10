package com.yash.trading.model;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data

public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @OneToOne // defining the relation b/w user entity and wallet entity one user one wallet
    private User user;

    private BigDecimal balance = BigDecimal.ZERO;


}

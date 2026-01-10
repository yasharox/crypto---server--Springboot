package com.yash.trading.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yash.trading.domain.TransactionType;
import com.yash.trading.domain.WalletTransactionType;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"wallet"})
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50)
    private WalletTransactionType type;

    private LocalDate date;

    private String transferId;

    private Long receiverWalletId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", length = 50)
    private TransactionType transactionType;

    private Long amount;

    private String purpose;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.date = LocalDate.now();
    }
}

package com.yash.trading.service;

import com.yash.trading.domain.TransactionType;
import com.yash.trading.domain.WalletTransactionType;
import com.yash.trading.model.Wallet;
import com.yash.trading.model.WalletTransaction;

import java.util.List;

public interface TransactionService {

    WalletTransaction createTransaction(
            Wallet wallet,
            TransactionType transactionType,
            Long receiverWalletId,
            WalletTransactionType walletTransfer, String purpose,
            Long amount
    );

    List<WalletTransaction> getTransactionsByWallet(Wallet wallet);
}

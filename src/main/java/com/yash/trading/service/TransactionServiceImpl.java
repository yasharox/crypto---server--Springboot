package com.yash.trading.service;

import com.yash.trading.domain.TransactionType;
import com.yash.trading.domain.WalletTransactionType;
import com.yash.trading.model.Wallet;
import com.yash.trading.model.WalletTransaction;
import com.yash.trading.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public WalletTransaction createTransaction(
            Wallet wallet,
            TransactionType transactionType,
            Long receiverWalletId,
            WalletTransactionType walletTransfer, String purpose,
            Long amount
    ) {

        WalletTransaction transaction = WalletTransaction.builder()
                .wallet(wallet)
                .transactionType(transactionType)
                .receiverWalletId(receiverWalletId)
                .purpose(purpose)
                .amount(amount)
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    public List<WalletTransaction> getTransactionsByWallet(Wallet wallet) {
        return transactionRepository.findByWalletOrderByCreatedAtDesc(wallet);
    }
}

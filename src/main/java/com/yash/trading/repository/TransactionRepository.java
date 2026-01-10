package com.yash.trading.repository;

import com.yash.trading.model.Wallet;
import com.yash.trading.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<WalletTransaction, Long>{

       List<WalletTransaction> findByWalletOrderByCreatedAtDesc(Wallet wallet);
}





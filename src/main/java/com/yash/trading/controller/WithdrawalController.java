package com.yash.trading.controller;


import com.yash.trading.domain.TransactionType;
import com.yash.trading.domain.WalletTransactionType;
import com.yash.trading.model.User;
import com.yash.trading.model.Wallet;
import com.yash.trading.model.WalletTransaction;
import com.yash.trading.model.Withdrawal;
import com.yash.trading.service.TransactionService;
import com.yash.trading.service.UserService;
import com.yash.trading.service.WalletService;
import com.yash.trading.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;





    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawalRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization")String jwt) throws Exception {

        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7).trim();
        }

        User user=userService.findUserProfileByJwt(jwt);

        Wallet userWallet=walletService.getUserWallet(user);
        if (userWallet == null) {
            throw new RuntimeException("User wallet not found");
        }


        // 🔴 FIX 3: Validate balance BEFORE withdrawal
        if (userWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new RuntimeException("Insufficient wallet balance");
        }


        Withdrawal withdrawal=withdrawalService.requestWithdrawal(amount,user);
        walletService.addBalance(userWallet, -withdrawal.getAmount());

        WalletTransaction walletTransaction = transactionService.createTransaction(
                userWallet,
                TransactionType.WITHDRAWAL,
                null,
                WalletTransactionType.WALLET_TRANSFER,
                "bank account withdrawal",
                withdrawal.getAmount()
        );

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization")String jwt) throws Exception {

        User user=userService.findUserProfileByJwt(jwt);
        Withdrawal withdrawal=withdrawalService.proceedWithdrawal(id,accept);
        Wallet userWallet=walletService.getUserWallet(user);

        if(!accept){
            walletService.addBalance(userWallet, withdrawal.getAmount());
        }
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(

            @RequestHeader("Authorization")String authHeader) throws Exception {


        String jwt = authHeader.replace("Bearer", "").trim();

        User user=userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawal=
                withdrawalService.getUsersWithdrawalHistory(user);
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(

            @RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        List<Withdrawal> withdrawal=withdrawalService.getAllWithdrawalRequest();

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }


}

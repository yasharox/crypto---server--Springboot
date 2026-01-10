package com.yash.trading.controller;

import com.yash.trading.model.User;
import com.yash.trading.model.Wallet;
import com.yash.trading.model.WalletTransaction;
import com.yash.trading.service.TransactionService;
import com.yash.trading.service.UserService;
import com.yash.trading.service.WalletService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/api/transactions")
    public ResponseEntity <List <WalletTransaction>> getUserTransaction(
            @RequestHeader("Authorization")String authHeader
    ) throws Exception {
        String jwt = extractJwt(authHeader);

        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet= walletService.getUserWallet(user);

        List<WalletTransaction> transactions =
                transactionService.getTransactionsByWallet(wallet);
        return new ResponseEntity<>(transactions, HttpStatus.OK);

        
    }

    private String extractJwt(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }
        return authHeader.substring(7);
    }
}

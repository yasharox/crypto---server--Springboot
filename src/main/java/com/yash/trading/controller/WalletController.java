package com.yash.trading.controller;


import com.yash.trading.domain.TransactionType;
import com.yash.trading.domain.WalletTransactionType;
import com.yash.trading.model.*;
import com.yash.trading.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;


    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;



    @GetMapping
    public ResponseEntity<Wallet> getUserWallet (
            @RequestHeader ("Authorization") String authHeader) throws Exception {



        String jwt = authHeader.substring(7).trim();

        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }




    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction req
    ) throws Exception {

        String jwt = authHeader.substring(7).trim();

        User senderUser = userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(walletId);

        Wallet wallet = walletService.walletToWalletTransfer(
                senderUser,
                receiverWallet,
                req.getAmount()
        );

        // ✅ FIXED createTransaction call (6 args)
        transactionService.createTransaction(
                wallet,
                TransactionType.DEBIT,                 // 🔴 MISSING BEFORE
                receiverWallet.getId(),
                WalletTransactionType.WALLET_TRANSFER,
                req.getPurpose(),
                req.getAmount()
        );

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }





    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment (
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long orderId

    ) throws  Exception{

        String jwt = authHeader.substring(7).trim();

        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);

        Wallet wallet = walletService.payOrderPayment(order, user);

        return  new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }




    @PutMapping("/deposit")
    public ResponseEntity<Wallet> addBalanceToWallet (
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = "order_id") Long orderId,
            @RequestParam(name = "payment_id") String paymentId

    ) throws  Exception{

//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new RuntimeException("Invalid Authorization header"); // 🔴 FIX
//        }

        String jwt =authHeader.substring(7).trim();

        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet( user);

        PaymentOrder order = paymentService.getPaymentOrderById(orderId);
        Boolean status = paymentService.proceedPaymentOrder(order,paymentId);

       if (wallet.getBalance() == null){
           wallet.setBalance(BigDecimal.valueOf(0));
       }
       if (status){
           wallet = walletService.addBalance(wallet, order.getAmount());
       }
        return  new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }
}

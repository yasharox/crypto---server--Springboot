package com.yash.trading.controller;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.yash.trading.domain.PaymentMethod;
import com.yash.trading.model.PaymentOrder;
import com.yash.trading.model.User;
import com.yash.trading.model.Wallet;
import com.yash.trading.response.PaymentResponse;
import com.yash.trading.service.PaymentService;
import com.yash.trading.service.UserService;

import com.yash.trading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;


//    // changed new
//    @Autowired
//    private WalletService walletService;

    /// /above

    @PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount,
            @RequestHeader("Authorization") String authHeader   // 🔴 FIX: rename
    ) throws Exception, RazorpayException, StripeException {

        // 🔴🔥 FIX: REMOVE "Bearer " BEFORE PARSING
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String jwt = authHeader.substring(7).trim(); // ✅ RAW JWT ONLY

        User user = userService.findUserProfileByJwt(jwt);

        PaymentOrder order =
                paymentService.createOrder(user, amount, paymentMethod);

        PaymentResponse paymentResponse;

        if (paymentMethod == PaymentMethod.RAZORPAY) {
            paymentResponse =
                    paymentService.createRazorPaymentLink(
                            user, amount, order.getId());
        } else {
            paymentResponse =
                    paymentService.createStripePaymentLink(
                            user, amount, order.getId());
        }

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }


    ///  new changes added






}


/// in razor pay >> uupi done >>  payment_id  >>  paymnt succss
//>> razor pay will make pai call


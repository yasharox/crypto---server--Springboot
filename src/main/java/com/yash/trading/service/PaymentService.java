package com.yash.trading.service;

import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.yash.trading.domain.PaymentMethod;
import com.yash.trading.model.PaymentOrder;
import com.yash.trading.model.User;
import com.yash.trading.response.PaymentResponse;

public interface PaymentService {

    PaymentOrder createOrder (User user, Long amount, PaymentMethod paymentMethod);


    PaymentOrder getPaymentOrderById (Long id) throws Exception;

    Boolean proceedPaymentOrder ( PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse createRazorPaymentLink (User user, Long Amount, Long orderId) throws RazorpayException;

    PaymentResponse createStripePaymentLink (User user, Long amount, Long orderId) throws StripeException;



}

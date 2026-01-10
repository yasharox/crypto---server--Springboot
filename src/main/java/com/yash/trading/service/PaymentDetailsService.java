package com.yash.trading.service;

import com.yash.trading.model.PaymentDetails;
import com.yash.trading.model.User;

public interface PaymentDetailsService {

    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName,
                                            String ifsc,
                                            String bankName,
                                            User user);

    public PaymentDetails getUsersPaymentDetails (User user);

}

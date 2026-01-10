package com.yash.trading.service;

import com.yash.trading.model.PaymentDetails;
import com.yash.trading.model.User;
import com.yash.trading.repository.PaymentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService{

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName,
                                            String ifsc, String bankName,
                                            User user) {

        PaymentDetails paymentDetails =
                paymentDetailsRepository.findByUserId(user.getId());

//        PaymentDetails paymentDetails = new PaymentDetails();

        // ✅ IF NOT EXISTS → CREATE NEW
        if (paymentDetails == null) {
            paymentDetails = new PaymentDetails();
            paymentDetails.setUser(user);
        }


        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHolderName(accountHolderName);
        paymentDetails.setIfsc(ifsc);
        paymentDetails.setBankName(bankName);
//        paymentDetails.setUser(user);

        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUsersPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }
}

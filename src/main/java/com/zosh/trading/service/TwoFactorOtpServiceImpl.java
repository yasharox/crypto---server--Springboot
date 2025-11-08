package com.zosh.trading.service;

import com.zosh.trading.model.TwoFactorOTP;
import com.zosh.trading.model.User;
import com.zosh.trading.repository.TwoFactorOtpRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService{

    @Autowired
    private TwoFactorOtpRepository twoFactorOtpRepository;

    @Override
    public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt) {

        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
        twoFactorOTP.setOtp(otp);
        twoFactorOTP.setJwt(jwt);
        twoFactorOTP.setId(id);
        twoFactorOTP.setUser(user);
        return twoFactorOtpRepository.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUser(Long UserId) {
          return twoFactorOtpRepository.findByUserId(UserId);
    }

    @Override
    public TwoFactorOTP findById(String Id) {
        Optional<TwoFactorOTP>  twoFactorOtp = twoFactorOtpRepository.findById(Id);
        return twoFactorOtp.orElse(null);
    }


    @Override
    public boolean VerifyTwoFactorOtp(TwoFactorOTP twoFactorOTP, String otp) {
        return twoFactorOTP.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP) {

        twoFactorOtpRepository.delete(twoFactorOTP);

    }
}

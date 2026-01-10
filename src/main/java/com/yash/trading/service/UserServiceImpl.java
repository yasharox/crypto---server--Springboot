
package com.yash.trading.service;

import com.yash.trading.config.JwtProvider;
import com.yash.trading.domain.VerificationType;
import com.yash.trading.model.TwoFactorAuth;
import com.yash.trading.model.User;
import com.yash.trading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class UserServiceImpl implements  UserService{

    @Autowired
    private UserRepository userRepository;


    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {

        jwt=jwt.trim();
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);
        if ( user == null){
            throw new Exception( " User not found");

        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email);
        if ( user == null){
            throw new Exception( " User not found");

        }
        return user;
    }

    @Override
    public User findUserById(Long userId) throws Exception {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new Exception(" User not found");
        }
        return user.get();
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {

        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);


        user.setTwoFactorAuth(twoFactorAuth);

        return userRepository.save(user);
    }


    @Override
    public User updatePassword(User user, String newPassword) {

        user.setPassword(newPassword);

        return userRepository.save(user);
    }
}

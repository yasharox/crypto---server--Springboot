package com.yash.trading.controller;

import com.yash.trading.config.JwtProvider;
import com.yash.trading.model.TwoFactorOTP;
import com.yash.trading.model.User;
import com.yash.trading.repository.UserRepository;
import com.yash.trading.response.AuthResponse;
import com.yash.trading.service.CustomUserDetailService;
import com.yash.trading.service.EmailService;
import com.yash.trading.service.TwoFactorOtpService;
import com.yash.trading.service.WatchlistService;
import com.yash.trading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private WatchlistService watchlistService;

    @Autowired ( required = true)
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

        User isEmailExist = userRepository.findByEmail(user.getEmail());

        if (isEmailExist != null){
            throw new Exception("email already is already in use with another account");
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setFullName(user.getFullName());

        User savedUser = userRepository.save(newUser);
        watchlistService.createWatchlist(savedUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
//
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
        String jwt = JwtProvider.generateToken(auth);
//
//
////        if(user.getTwoFactorAuth().isEnabled())  << changes done here
        if (user.getTwoFactorAuth() != null && user.getTwoFactorAuth().isEnabled()) {

            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor auth is enabled");
            res.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOTP();
//
            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(user.getId());
        }

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("register success");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }




    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {


        String userName = user.getEmail();
        String password = user.getPassword();


        Authentication auth = authenticate (userName,password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);

        User authUser = userRepository.findByEmail(userName);

        if( user.getTwoFactorAuth().isEnabled()){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor auth is enabled");
            res.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generateOTP();

            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(authUser.getId());
            if(oldTwoFactorOTP != null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }

            TwoFactorOTP newTwoFActorOTP = twoFactorOtpService.createTwoFactorOtp(
                    authUser,otp,jwt);

            emailService.sendVerificationOtpEmail(userName, otp);


            res.setSession(newTwoFActorOTP.getId());
            return  new ResponseEntity<>(res, HttpStatus.ACCEPTED);


        }

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("login success");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }







    private Authentication authenticate(String userName, String password) {

        UserDetails userDetails = customUserDetailService.loadUserByUsername(userName);

        if (userDetails == null){
            throw new BadCredentialsException("invalid username"); // 1:56:22
        }

//        if (!password.equals(userDetails.getPassword())){
//            throw new BadCredentialsException("invalid password");
//        }
        if (password == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

    }

    @PostMapping( "/two-factor/otp/{otp}")
    public  ResponseEntity < AuthResponse> verifySigninOtp (
            @PathVariable String otp,
            @RequestParam String id) throws Exception {

        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);

        if(twoFactorOtpService.VerifyTwoFactorOtp(twoFactorOTP, otp)){
            AuthResponse res = new AuthResponse();
            res.setMessage(" Two factor authentication verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new  Exception( " invalid otp");
    }
}
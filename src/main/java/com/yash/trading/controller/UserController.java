//user controller

package com.yash.trading.controller;

import com.yash.trading.request.ForgotPasswordTokenRequest;
import com.yash.trading.domain.VerificationType;
import com.yash.trading.model.ForgetPasswordToken;
import com.yash.trading.model.User;
import com.yash.trading.model.VerificationCode;
import com.yash.trading.request.ResetPasswordRequest;
import com.yash.trading.response.ApiResponse;
import com.yash.trading.response.AuthResponse;
import com.yash.trading.service.EmailService;
import com.yash.trading.service.ForgotPasswordService;
import com.yash.trading.service.UserService;
import com.yash.trading.service.VerificationCodeService;
import com.yash.trading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    /* private String jwt; //  if req then uncomment it */

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(
            @RequestHeader("Authorization") String authHeader) throws Exception {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String jwt = authHeader.replace("Bearer", "").trim(); // ✅ SAFE

        User user = userService.findUserProfileByJwt(jwt);

        return new  ResponseEntity<User>(user, HttpStatus.OK);
    }




    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp (
            @RequestHeader("Authorization") String jwt,
            @PathVariable VerificationType verificationType) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        if ( verificationCode == null ){

            verificationCode = verificationCodeService
                    .sendVerificationCode(user, verificationType);
        }
        if ( verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp() );
        }
        return new ResponseEntity<>("verification otp sent successfully", HttpStatus.OK);

    }


    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication (

            @PathVariable String otp,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo =  verificationCode.getVerificationType().equals(VerificationType.EMAIL)?
                verificationCode.getEmail(): verificationCode.getMobile();


        boolean isVerified = verificationCode.getOtp().equals(otp);


        if(isVerified){
            User uodateUser = userService.enableTwoFactorAuthentication(
                    verificationCode.getVerificationType(),sendTo, user);

            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return new ResponseEntity<>(uodateUser, HttpStatus.OK);
        }
        throw new Exception("wrong otp");

    }



    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp (

            @RequestBody ForgotPasswordTokenRequest req ) throws Exception {

        User user = userService.findUserByEmail(req.getSendTo());

        String otp = OtpUtils.generateOTP();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgetPasswordToken token = forgotPasswordService.findByUser(user.getId());

        if ( token == null){

            token = forgotPasswordService.createToken(user,id,otp, req.getVerificationType(), req.getSendTo());
        }

        if ( req.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(user.getEmail(),token.getOtp());
        }

        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset otp sent successfully");


        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @PatchMapping("/auth/users/reset-password/verify-otp/")
    public ResponseEntity<ApiResponse> resetPassword(

            @RequestParam String id,
            @RequestBody ResetPasswordRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {


        ForgetPasswordToken forgetPasswordToken = forgotPasswordService.findById(id);

        boolean isVerified = forgetPasswordToken.getOtp().equals(req.getOtp());


        if (isVerified){

            userService.updatePassword(forgetPasswordToken.getUser(), req.getPassword());
            ApiResponse res = new ApiResponse();
            res.setMessage("password update successfully");
            return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
        }

        throw new Exception("wrong otp");

    }

}
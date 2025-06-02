package com.gig.config;

import com.gig.applicationUtilities.ApplicationConstants;
import com.gig.dto.EmailDto;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;


@Service
public class OTPService {
    private final Logger LOGGER = LoggerFactory.getLogger(OTPService.class);
    @Autowired
    OTPGenerator otpGenerator;
    @Autowired
    EmailService emailService;
    public Map<String, Object> generateOtp(String key, EmailDto emailDto) {
        Map<String, Object> resource = new HashMap<>();
        Boolean isMailSent = true;
        resource.put(ApplicationConstants.IS_MAIL_SENT, isMailSent);
        Integer otp = otpGenerator.generateOTP(key);
        if (otp == -1)
        {
            LOGGER.error("OTPService::generateOtp:: OTP generator is not working...");
            return  resource;
        }
        LOGGER.info("OTPService::generateOtp:: Generated OTP: {}", otp);
        try {
            Context context = new Context();
            emailDto.setRecipient(key);
            context.setVariable("otp", otp);
            emailService.sendMail(emailDto, context);
            resource.put("otp", otp);
        } catch (Exception e) {
            LOGGER.info("OTPService::generateOtp::Failed to send mail: ", e.getLocalizedMessage());
            resource.put("isMailSent", false);
        }
        return resource;
    }
    public Map<String, Object> validateOTP(String key, Integer otpNumber)
    {
        Map<String, Object> resource = new HashMap<>();
        // get OTP from cache
        Integer cacheOTP = otpGenerator.getOPTByKey(key);
        resource.put("expired",false);
        resource.put("invalid",false);
        resource.put("success",false);
        if (cacheOTP!=null && cacheOTP.equals(otpNumber))
        {
            otpGenerator.clearOTPFromCache(key);
            resource.put("success",true);
        } else if(ObjectUtils.isEmpty(cacheOTP)) {
            resource.put("expired",true);
        } else if(!cacheOTP.equals(otpNumber)) {
            resource.put("invalid",true);
        }
        return resource;
    }
}

package com.gig.config;

import org.springframework.stereotype.Service;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;


import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPGenerator {

    private static final Integer EXPIRE_MIN = 15;
    private LoadingCache<String, Integer> otpCache;
    public OTPGenerator()
    {
        super();
        otpCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });

    }
    public Integer generateOTP(String key) {
        Random random = new Random();
        int OTP = 100000 + random.nextInt(900000);
        otpCache.put(key, OTP);

        return OTP;
    }

    public Integer getOPTByKey(String key) {
        return otpCache.getIfPresent(key);
    }

    public void clearOTPFromCache(String key) {
        otpCache.invalidate(key);
    }

}

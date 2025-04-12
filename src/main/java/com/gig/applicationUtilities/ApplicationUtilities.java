package com.gig.applicationUtilities;

import com.gig.enums.LoginStatusEnum;
import com.gig.models.Login;
import com.gig.models.Member;
import com.gig.repository.LoginRepository;
import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Component
public class ApplicationUtilities {

    private final Key key;
    private final Cipher cipher;
    @Autowired
    private LoginRepository loginRepository;
    private static final String AES = "AES";
    private static final String SECRET = "secret-key-12345";
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public ApplicationUtilities() throws NoSuchPaddingException, NoSuchAlgorithmException {
        key =new SecretKeySpec(SECRET.getBytes(), AES);
        cipher = Cipher.getInstance(AES);
    }

    public String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Boolean isPasswordMatched(String decryptedPassword, String encryptedPassword) {
        try {
            return passwordEncoder.matches(decryptedPassword, encryptedPassword);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Transactional
    public Member getLoggedInUser(HttpServletRequest request) {
        Login login = getLogin(request);
        Member member = login.getMember();
        Assert.notNull(member, "No such member exists!");
        Assert.isTrue(!member.getIsDeleted(),"Member not found.");
        return member;
    }


    public Login getLogin(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        Assert.notNull(header, "No Authorization Token!");
        String authToken = header.replace(ApplicationConstants.TOKEN_PREFIX, "").trim();
        Login login = loginRepository.findByTokenAndStatus(authToken, LoginStatusEnum.LOGGED_IN.toString());
        Assert.notNull(login, "Couldn't find login..!");
        return login;
    }
}

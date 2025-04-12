package com.gig.config;

import com.gig.applicationUtilities.ApplicationConstants;
import com.gig.models.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtTokenUtils {
    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Object getScopesFromToken(String token) {
        return getAllClaimsFromToken(token).get("scopes");
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(ApplicationConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        if(ObjectUtils.isNotEmpty(expiration))
            return new Date().before(expiration);
        else
            return null;
    }

    public String generateToken(Member member, HttpServletRequest request) {
        return doGenerateToken(member, request);
    }


    private String doGenerateToken(Member member, HttpServletRequest request) {
        Claims claims = Jwts.claims().setSubject(member.getEmailAddress());
        LocalDateTime localDateTime = LocalDateTime.now();
        Date issuedAt = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .signWith(SignatureAlgorithm.HS256, ApplicationConstants.SECRET);
        return jwtBuilder.compact();
    }


    public String validateToken(String token) {
        final String email = getEmailFromToken(token);
        Assert.notNull(email, "Email doesn't exist in the token");
        return email;
    }


}

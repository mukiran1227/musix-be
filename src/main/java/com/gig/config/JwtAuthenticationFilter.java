package com.gig.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gig.applicationUtilities.ApplicationConstants;
import com.gig.enums.LoginStatusEnum;
import com.gig.models.Login;
import com.gig.repository.LoginRepository;
import com.gig.service.LoginService;
import com.gig.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private LoginRepository loginRepository;
    private MemberService memberService;

    private LoginService loginService;
    private JwtTokenUtils jwtTokenUtils;

    JwtAuthenticationFilter(MemberService memberService, JwtTokenUtils jwtTokenUtil, LoginRepository loginRepository,LoginService loginService){
        this.memberService = memberService;
        this.jwtTokenUtils = jwtTokenUtil;
        this.loginService = loginService;
        this.loginRepository = loginRepository;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Login login=null;
        try {
            String header = request.getHeader("Authorization");
            Assert.notNull(header, "There is no token in header...");
            String authToken = null;
            if (header != null && header.startsWith(ApplicationConstants.TOKEN_PREFIX)) {

                authToken = header.replace(ApplicationConstants.TOKEN_PREFIX,"").trim();
                login = loginService.findByTokenAndStatus(authToken, LoginStatusEnum.LOGGED_IN);
                Assert.notNull(login, "Login not found!");
                Boolean jwtExpired = jwtTokenUtils.isTokenExpired(authToken);
                if(ObjectUtils.isNotEmpty(jwtExpired))
                    Assert.isTrue(jwtExpired, "TOKEN EXPIRED");
                Assert.notNull(login.getMember(), "User doesn't exist with provided token");
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(login.getMember(), null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
//                log.warn("couldn't find bearer string, will ignore the header");

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                ResponseEntity<String> resp = new ResponseEntity<>("couldn't find bearer string, will ignore the header", HttpStatus.FORBIDDEN);
                String jsonRespString = ow.writeValueAsString(resp);
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(jsonRespString);
            }

        } catch(ExpiredJwtException expiredJwtException) {
            if(ObjectUtils.isNotEmpty(login)) {

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                ResponseEntity<String> resp = new ResponseEntity<>("Login Expired", HttpStatus.UNAUTHORIZED);
                String jsonRespString = ow.writeValueAsString(resp);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(jsonRespString);
                login.setStatus(LoginStatusEnum.LOGGED_OUT);
                login.setLoggedOutTime(LocalDateTime.now());
                loginRepository.save(login);
            }
        } catch(Exception e){
            e.printStackTrace();
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            ResponseEntity<String> resp = new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            String jsonRespString = ow.writeValueAsString(resp);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(jsonRespString);
        }
    }
}

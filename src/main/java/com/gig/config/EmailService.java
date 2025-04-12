package com.gig.config;

import com.gig.dto.EmailDto;
import com.gig.exceptions.ApiException;
import jakarta.activation.URLDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {
    Logger log = Logger.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private String emailFrom;

    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    public void sendMail(EmailDto emailDto, Context context) {
        log.info("EmailService::sendMail:: Preparing to send email to: "+ emailDto.getRecipient().toLowerCase() +" with subject:" +emailDto.getSubject());
        try {
            MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            mimeMessageHelper.setTo(emailDto.getRecipient().toLowerCase());
            mimeMessageHelper.setFrom(emailFrom);
            mimeMessageHelper.setSubject(emailDto.getSubject());
            mimeMessageHelper.setText(getStringFromTemplate(emailDto.getTemplateName(), context), true);
//            mimeMessageHelper.addInline("logo", new URLDataSource(new URI("https://via.placeholder.com/150x50?text=Gig+Logo").toURL()));
            CompletableFuture.supplyAsync(() -> {
                try {
                    javaMailSender.send(mimeMailMessage);
                    log.info("EmailService::sendMail::Email sent successfully to: "+ emailDto.getRecipient().toLowerCase() +" with subject:" +emailDto.getSubject());
                } catch (Exception e){
                    log.error("EmailService::sendMail::Failed to send email to: "+ emailDto.getRecipient().toLowerCase()+ " with subject: " +emailDto.getSubject(), e);
                    e.printStackTrace();
                    throw new ApiException(e.getLocalizedMessage());
                }
                return null;
            });
        } catch (MessagingException  e) {
            log.error("EmailService::sendMail::Failed to send email to: "+ emailDto.getRecipient().toLowerCase() +  " with subject: " +emailDto.getSubject(), e);
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
        }
    }
    public String getStringFromTemplate(String templateName, Context context){
        return templateEngine.process(templateName, context);
    }
 }

package com.jarek4313.auth.services;

import com.google.common.io.Files;
import com.jarek4313.auth.configuration.EmailConfiguration;
import com.jarek4313.auth.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final EmailConfiguration emailConfiguration;
    @Value("${front.url}")
    private String frontendUrl;
    @Value("classpath:static/mail-active.html")
    private Resource mailActiveTemplate;
    @Value("classpath:static/reset-password.html")
    private Resource resetPasswordTemplate;

    public void sendActivation(User user) {
        log.info("--START send activation");
        try {
            String html = createHtmlFromFile(mailActiveTemplate.getFile());
            html = html.replace("https://google.com", frontendUrl+"/activate/"+user.getUuid());
            emailConfiguration.sendMail(user.getEmail(), html, "Account Activation", true);
        } catch (IOException e) {
            log.warn("Cant send mail");
            throw new RuntimeException(e);
        }
        log.info("--STOP send activation");
    }

    public void sendPasswordRecovery(User user, String uuid) {
        try {
            log.info("--START send password recovery");
            String html = createHtmlFromFile(resetPasswordTemplate.getFile());
            html = html.replace("https://google.com", frontendUrl + "/odzyskaj-haslo/" + uuid);
            emailConfiguration.sendMail(user.getEmail(), html, "Odzyskanie hasła", true);
        } catch (IOException e) {
            log.info("Cand send mail");
            throw new RuntimeException(e);
        }
    }

    private String createHtmlFromFile(File file) throws IOException {
        return Files.toString(file, StandardCharsets.UTF_8);
    }
}

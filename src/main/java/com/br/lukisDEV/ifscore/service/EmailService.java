package com.br.lukisDEV.ifscore.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String token) {

        try {

            String verificationLink =
                    "https://ifscore.onrender.com/v1/auth/verify?token=" + token;

            String html = loadTemplate();

            html = html.replace(
                    "{{verificationLink}}",
                    verificationLink
            );

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("🏆 Confirme sua conta no IFScore");

            helper.setText(html, true);

            mailSender.send(message);

            System.out.println("Email enviado com sucesso!");

        } catch (Exception e) {

            System.out.println("Erro ao enviar email:");
            e.printStackTrace();

        }
    }

    private String loadTemplate() throws IOException {

        InputStream inputStream =
                getClass().getResourceAsStream("/templates/email.html");

        if (inputStream == null) {
            throw new RuntimeException(
                    "Arquivo email.html não encontrado em resources/templates"
            );
        }

        return new String(
                inputStream.readAllBytes(),
                StandardCharsets.UTF_8
        );
    }
}
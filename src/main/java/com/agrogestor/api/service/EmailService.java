package com.agrogestor.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${MAIL_FROM}")
    private String mailFrom;

    /**
     * URL base do frontend (ex: https://agrogestor-br.vercel.app).
     * Vem da variável de ambiente APP_FRONTEND_URL.
     */
    @Value("${app.frontend-url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailConfirmacao(
            String email,
            String nome,
            String token
    ) {
        // ✅ Link aponta para o FRONTEND (que trata e mostra a página de status)
        String link = frontendUrl + "/confirmar?token=" + token;

        SimpleMailMessage mensagem = new SimpleMailMessage();

        mensagem.setFrom(mailFrom);
        mensagem.setTo(email);

        mensagem.setSubject("Confirmação de cadastro - AgroGestor");

        mensagem.setText(
                """
                Olá %s,

                Seja bem-vindo ao AgroGestor!

                Para ativar sua conta, clique no link abaixo:

                %s

                Este link é válido por 24 horas.

                Se você não criou esta conta, ignore este email.

                Equipe AgroGestor
                """
                        .formatted(nome, link)
        );

        mailSender.send(mensagem);
    }
}
package com.agrogestor.api.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void enviarEmailConfirmacao(
            String email,
            String nome,
            String token
    ) {


        String link =
                "https://agrogestor-api.duckdns.org/api/auth/confirmar?token="
                        + token;


        SimpleMailMessage mensagem = new SimpleMailMessage();


        mensagem.setFrom("SEU_EMAIL_REMETENTE_VERIFICADO");

        mensagem.setTo(email);


        mensagem.setSubject(
                "Confirmação de cadastro - AgroGestor"
        );


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
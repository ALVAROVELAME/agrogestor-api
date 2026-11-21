package com.agrogestor.api.service;

import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class EmailServiceTest {
    @Test void enviaLinkDeRecuperacaoAoDestinatario() {
        JavaMailSender sender = mock(JavaMailSender.class);
        EmailService service = new EmailService(sender);
        ReflectionTestUtils.setField(service, "mailFrom", "teste@example.com");
        ReflectionTestUtils.setField(service, "frontendUrl", "https://frontend.example.com");
        service.enviarEmailRecuperacao("maria@example.com", "token");
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(sender).send(captor.capture());
        assertThat(captor.getValue().getTo()).containsExactly("maria@example.com");
        assertThat(captor.getValue().getText()).contains("https://frontend.example.com/reset-password?token=token", "30 minutos");
    }
    @Test void enviaLinkParaFrontendSemExporSenha() {
        JavaMailSender sender = mock(JavaMailSender.class);
        EmailService service = new EmailService(sender);
        ReflectionTestUtils.setField(service, "mailFrom", "teste@example.com");
        ReflectionTestUtils.setField(service, "frontendUrl", "https://frontend.example.com");
        service.enviarEmailConfirmacao("maria@example.com", "Maria", "token");
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(sender).send(captor.capture());
        assertThat(captor.getValue().getTo()).containsExactly("maria@example.com");
        assertThat(captor.getValue().getFrom()).isEqualTo("teste@example.com");
        assertThat(captor.getValue().getText()).contains("Maria", "https://frontend.example.com/confirmar?token=token", "24 horas");
    }
}

package com.agrogestor.api.service.auth;

import com.agrogestor.api.service.EmailService;

import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.repository.conta.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecuperacaoSenhaServiceTest {
    UsuarioRepository usuarios = mock(UsuarioRepository.class);
    PasswordEncoder encoder = mock(PasswordEncoder.class);
    EmailService email = mock(EmailService.class);
    RecuperacaoSenhaService service = new RecuperacaoSenhaService(usuarios, encoder, email);

    @Test void geraTokenSeguroEArmazenaSomenteHash() {
        Usuario usuario = new Usuario("Maria", "maria@example.com", "hash");
        usuario.ativarConta();
        when(usuarios.buscarPorEmailParaRecuperacao(usuario.getEmail())).thenReturn(Optional.of(usuario));
        service.solicitar(usuario.getEmail());
        ArgumentCaptor<String> token = ArgumentCaptor.forClass(String.class);
        verify(email).enviarEmailRecuperacao(eq(usuario.getEmail()), token.capture());
        assertThat(token.getValue()).hasSize(43);
        assertThat(usuario.getRecuperacaoTokenHash()).isEqualTo(RecuperacaoSenhaService.hash(token.getValue())).isNotEqualTo(token.getValue());
        assertThat(usuario.getRecuperacaoExpiraEm()).isAfter(LocalDateTime.now());
        service.solicitar(usuario.getEmail());
        verify(email, times(1)).enviarEmailRecuperacao(anyString(), anyString());
    }
    @Test void emailDesconhecidoNaoEnviaMensagem() {
        service.solicitar("naoexiste@example.com");
        verifyNoInteractions(email);
    }
    @Test void tokenExpiradoNaoMudaSenha() {
        Usuario usuario = new Usuario("Maria", "maria@example.com", "hash");
        usuario.ativarConta();
        usuario.iniciarRecuperacao(RecuperacaoSenhaService.hash("token"), LocalDateTime.now().minusMinutes(31));
        when(usuarios.buscarPorTokenDeRecuperacao(RecuperacaoSenhaService.hash("token"))).thenReturn(Optional.of(usuario));
        assertThat(service.redefinir("token", "novaSenha")).isFalse();
        assertThat(usuario.getSenhaHash()).isEqualTo("hash");
    }
    @Test void trocaSenhaConsomeTokenEInvalidaSessoes() {
        Usuario usuario = new Usuario("Maria", "maria@example.com", "hash");
        usuario.ativarConta();
        usuario.iniciarRecuperacao(RecuperacaoSenhaService.hash("token"), LocalDateTime.now());
        when(usuarios.buscarPorTokenDeRecuperacao(RecuperacaoSenhaService.hash("token")))
                .thenReturn(Optional.of(usuario)).thenReturn(Optional.empty());
        when(encoder.encode("novaSenha")).thenReturn("novo-hash");
        assertThat(service.redefinir("token", "novaSenha")).isTrue();
        assertThat(usuario.getSenhaHash()).isEqualTo("novo-hash");
        assertThat(usuario.getRecuperacaoTokenHash()).isNull();
        assertThat(usuario.getVersaoSessao()).isEqualTo(1);
        assertThat(service.redefinir("token", "novaSenha")).isFalse();
    }
}

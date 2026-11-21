package com.agrogestor.api.service.auth;

import com.agrogestor.api.dto.auth.LoginDTO;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.repository.conta.UsuarioRepository;
import com.agrogestor.api.repository.conta.CadastroPendenteRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UsuarioRepository usuarios;
    @Mock CadastroPendenteRepository pendentes;
    @Mock PasswordEncoder encoder;
    @InjectMocks AuthService service;
    LoginDTO dto;
    Usuario usuario;

    @BeforeEach void preparar() {
        dto = new LoginDTO(); dto.setEmail("maria@example.com"); dto.setSenha("segredo");
        usuario = new Usuario("Maria", dto.getEmail(), "hash");
    }
    @Test void autenticaContaAtiva() {
        usuario.ativarConta();
        when(usuarios.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        when(encoder.matches("segredo", "hash")).thenReturn(true);
        assertThat(service.autenticar(dto)).isSameAs(usuario);
    }
    @Test void rejeitaEmailInexistente() {
        assertThatThrownBy(() -> service.autenticar(dto)).isInstanceOf(BadCredentialsException.class);
    }
    @Test void rejeitaSenhaIncorreta() {
        usuario.ativarConta();
        when(usuarios.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        assertThatThrownBy(() -> service.autenticar(dto)).isInstanceOf(BadCredentialsException.class);
    }
    @Test void rejeitaContaInativa() {
        when(usuarios.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        when(encoder.matches("segredo", "hash")).thenReturn(true);
        assertThatThrownBy(() -> service.autenticar(dto)).isInstanceOf(DisabledException.class);
    }
    @Test void naoRevelaStatusDaContaComSenhaIncorreta() {
        when(usuarios.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        assertThatThrownBy(() -> service.autenticar(dto)).isInstanceOf(BadCredentialsException.class);
    }
}

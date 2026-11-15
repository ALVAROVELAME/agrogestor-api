package com.agrogestor.api.service;

import com.agrogestor.api.dto.UsuarioCadastroDTO;
import com.agrogestor.api.model.*;
import com.agrogestor.api.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock CadastroPendenteRepository pendentes;
    @Mock UsuarioRepository usuarios;
    @Mock AnimalRepository animais;
    @Mock PasswordEncoder encoder;
    @Mock EmailService email;
    @InjectMocks UsuarioService service;
    UsuarioCadastroDTO dto;
    @BeforeEach void preparar() {
        dto = new UsuarioCadastroDTO(); dto.setNome("Maria");
        dto.setEmail("maria@example.com"); dto.setSenha("segredo");
    }
    @Test void cadastraComHashETokenUnico() {
        when(encoder.encode("segredo")).thenReturn("hash");
        when(pendentes.save(any())).thenAnswer(i -> i.getArgument(0));
        CadastroPendente salvo = service.cadastrar(dto);
        assertThat(salvo.getSenhaHash()).isEqualTo("hash");
        assertThat(salvo.getTokenConfirmacao()).isNotBlank();
        assertThat(salvo.getTokenExpiraEm()).isAfter(salvo.getCriadoEm());
        verify(email).enviarEmailConfirmacao(dto.getEmail(), dto.getNome(), salvo.getTokenConfirmacao());
        verify(usuarios, never()).save(any());
    }
    @Test void rejeitaEmailCadastrado() {
        when(usuarios.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario()));
        assertThatThrownBy(() -> service.cadastrar(dto)).hasMessage("Email já cadastrado");
        verifyNoInteractions(encoder, email);
    }
    @Test void rejeitaCadastroAindaValido() {
        when(pendentes.findByEmail(dto.getEmail())).thenReturn(Optional.of(new CadastroPendente("Maria", dto.getEmail(), "hash", "token")));
        assertThatThrownBy(() -> service.cadastrar(dto)).hasMessage("Email aguardando confirmação");
        verifyNoInteractions(encoder, email);
    }
    @Test void senhaErradaNaoExcluiDados() {
        assertThatThrownBy(() -> service.excluirConta(usuario(), "errada")).isInstanceOf(BadCredentialsException.class);
        verifyNoInteractions(animais, usuarios, pendentes);
    }
    @Test void excluiDependenciasAntesDaConta() {
        Usuario usuario = usuario();
        when(encoder.matches("segredo", "hash")).thenReturn(true);
        service.excluirConta(usuario, "segredo");
        InOrder ordem = inOrder(animais, usuarios);
        ordem.verify(animais).deleteByUsuarioId(7L);
        ordem.verify(usuarios).delete(usuario);
    }
    private Usuario usuario() {
        Usuario u = new Usuario("Maria", dto.getEmail(), "hash");
        ReflectionTestUtils.setField(u, "id", 7L); return u;
    }
}

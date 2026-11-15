package com.agrogestor.api.service;

import com.agrogestor.api.model.*;
import com.agrogestor.api.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailConfirmationServiceTest {
    @Mock CadastroPendenteRepository pendentes;
    @Mock UsuarioRepository usuarios;
    @InjectMocks EmailConfirmationService service;

    @Test void tokenInexistenteNaoCriaUsuario() {
        assertThat(service.confirmarEmail("inexistente")).isFalse();
        verifyNoInteractions(usuarios);
    }
    @Test void tokenExpiradoNaoCriaUsuario() {
        CadastroPendente cadastro = cadastro();
        ReflectionTestUtils.setField(cadastro, "tokenExpiraEm", LocalDateTime.now().minusSeconds(1));
        when(pendentes.findByTokenConfirmacao("token")).thenReturn(Optional.of(cadastro));
        assertThat(service.confirmarEmail("token")).isFalse();
        verifyNoInteractions(usuarios);
        verify(pendentes, never()).delete(any());
    }
    @Test void tokenSemExpiracaoNaoCriaUsuario() {
        CadastroPendente cadastro = cadastro();
        ReflectionTestUtils.setField(cadastro, "tokenExpiraEm", null);
        when(pendentes.findByTokenConfirmacao("token")).thenReturn(Optional.of(cadastro));
        assertThat(service.confirmarEmail("token")).isFalse();
        verifyNoInteractions(usuarios);
    }
    @Test void confirmaEConsomeToken() {
        CadastroPendente cadastro = cadastro();
        when(pendentes.findByTokenConfirmacao("token")).thenReturn(Optional.of(cadastro));
        assertThat(service.confirmarEmail("token")).isTrue();
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarios).save(captor.capture());
        assertThat(captor.getValue().getAtivo()).isTrue();
        assertThat(captor.getValue().getSenhaHash()).isEqualTo("hash");
        assertThat(captor.getValue().getEmailConfirmadoEm()).isNotNull();
        verify(pendentes).delete(cadastro);
    }
    private CadastroPendente cadastro() { return new CadastroPendente("Maria", "maria@example.com", "hash", "token"); }
}

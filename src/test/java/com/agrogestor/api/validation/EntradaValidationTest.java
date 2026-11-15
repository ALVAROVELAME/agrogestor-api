package com.agrogestor.api.validation;

import com.agrogestor.api.dto.*;
import jakarta.validation.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.*;

class EntradaValidationTest {
    static ValidatorFactory factory;
    static Validator validator;
    @BeforeAll static void iniciar() { factory = Validation.buildDefaultValidatorFactory(); validator = factory.getValidator(); }
    @AfterAll static void fechar() { factory.close(); }
    @ParameterizedTest @ValueSource(strings = {"a", "á", "🐄"})
    void senhaNaoPodeExceder72Bytes(String caractere) {
        UsuarioCadastroDTO dto = cadastro(caractere.repeat(73 / caractere.getBytes(java.nio.charset.StandardCharsets.UTF_8).length + 1));
        assertThat(validator.validate(dto)).anyMatch(v -> v.getMessage().contains("72 bytes"));
    }
    @Test void aceitaSenhaNoLimiteDeBytes() { assertThat(validator.validate(cadastro("á".repeat(36)))).isEmpty(); }
    @Test void limitaSenhaTambemNoLoginEExclusao() {
        LoginDTO login = new LoginDTO(); login.setEmail("maria@example.com"); login.setSenha("a".repeat(73));
        ExcluirContaDTO excluir = new ExcluirContaDTO(); excluir.setSenha("a".repeat(73));
        assertThat(validator.validate(login)).isNotEmpty(); assertThat(validator.validate(excluir)).isNotEmpty();
    }
    private UsuarioCadastroDTO cadastro(String senha) {
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO(); dto.setNome("Maria"); dto.setEmail("maria@example.com"); dto.setSenha(senha); return dto;
    }
}

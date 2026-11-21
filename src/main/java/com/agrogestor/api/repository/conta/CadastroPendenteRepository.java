package com.agrogestor.api.repository.conta;

import com.agrogestor.api.model.conta.CadastroPendente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CadastroPendenteRepository
        extends JpaRepository<CadastroPendente, Long> {


    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    Optional<CadastroPendente> findByTokenConfirmacao(String token);


    Optional<CadastroPendente> findByEmail(String email);


}
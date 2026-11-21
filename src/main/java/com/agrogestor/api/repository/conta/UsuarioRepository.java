package com.agrogestor.api.repository.conta;


import com.agrogestor.api.model.conta.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from Usuario u where u.email = :email")
    Optional<Usuario> buscarPorEmailParaRecuperacao(@Param("email") String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from Usuario u where u.recuperacaoTokenHash = :hash")
    Optional<Usuario> buscarPorTokenDeRecuperacao(@Param("hash") String hash);





}

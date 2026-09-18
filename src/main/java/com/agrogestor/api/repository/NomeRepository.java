package com.agrogestor.api.repository;

import com.agrogestor.api.model.Nome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NomeRepository extends JpaRepository<Nome, Long> {

    List<Nome> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    Optional<Nome> findByIdAndUsuarioId(Long id, Long usuarioId);

    @Modifying
    @Query("DELETE FROM Nome n WHERE n.usuario.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);
}
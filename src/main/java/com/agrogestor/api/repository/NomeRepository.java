package com.agrogestor.api.repository;

import com.agrogestor.api.model.Nome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NomeRepository extends JpaRepository<Nome, Long> {

    List<Nome> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);

    Optional<Nome> findByIdAndUsuarioId(Long id, Long usuarioId);
}
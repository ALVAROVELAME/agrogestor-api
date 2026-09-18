package com.agrogestor.api.repository;

import com.agrogestor.api.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);

    Optional<Animal> findByIdAndUsuarioId(Long id, Long usuarioId);

    @Modifying
    @Query("DELETE FROM Animal a WHERE a.usuario.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);
}
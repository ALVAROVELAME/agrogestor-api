package com.agrogestor.api.repository;

import com.agrogestor.api.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    /** Lista apenas os animais de um usuário específico. */
    List<Animal> findByUsuarioIdOrderByCriadoEmDesc(Long usuarioId);

    /** Busca animal por ID **e** usuário (impede acesso cruzado). */
    Optional<Animal> findByIdAndUsuarioId(Long id, Long usuarioId);
}
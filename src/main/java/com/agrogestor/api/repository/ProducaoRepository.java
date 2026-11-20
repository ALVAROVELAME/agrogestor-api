package com.agrogestor.api.repository;

import com.agrogestor.api.model.Producao;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProducaoRepository extends JpaRepository<Producao, Long> {
    Optional<Producao> findByIdAndUsuarioId(Long id, Long usuarioId);
    @Query("select p from Producao p join fetch p.animal where p.usuario.id = :usuarioId "
            + "and (:inicio is null or p.data >= :inicio) and (:fim is null or p.data <= :fim) "
            + "and (:animalId is null or p.animal.id = :animalId) and (:turno is null or p.turno = :turno)")
    Page<Producao> buscar(@Param("usuarioId") Long usuarioId, @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim, @Param("animalId") Long animalId,
            @Param("turno") Producao.Turno turno, Pageable pageable);

    @Query("select p.data, sum(p.litros), count(p.id) from Producao p where p.usuario.id = :usuarioId "
            + "and p.data >= :inicio and p.data <= :fim and (:animalId is null or p.animal.id = :animalId) group by p.data order by p.data")
    List<Object[]> totalPorDia(@Param("usuarioId") Long usuarioId, @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim, @Param("animalId") Long animalId);
}

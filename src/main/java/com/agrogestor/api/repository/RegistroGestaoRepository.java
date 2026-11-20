package com.agrogestor.api.repository;

import com.agrogestor.api.model.RegistroGestao;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RegistroGestaoRepository extends JpaRepository<RegistroGestao, Long> {
    Optional<RegistroGestao> findByIdAndUsuarioIdAndTipo(Long id, Long usuarioId, RegistroGestao.Tipo tipo);
    @Query("select r from RegistroGestao r left join fetch r.animal where r.usuario.id = :usuarioId and r.tipo = :tipo "
            + "and (:inicio is null or r.data >= :inicio) and (:fim is null or r.data <= :fim) "
            + "and (:animalId is null or r.animal.id = :animalId) and (:concluida is null or r.concluida = :concluida) "
            + "and (:subtipo is null or r.subtipo = :subtipo)")
    Page<RegistroGestao> buscar(@Param("usuarioId") Long usuarioId, @Param("tipo") RegistroGestao.Tipo tipo,
            @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim, @Param("animalId") Long animalId,
            @Param("concluida") Boolean concluida, @Param("subtipo") String subtipo, Pageable pagina);

    @Query("select r.subtipo, sum(r.valor), count(r.id) from RegistroGestao r where r.usuario.id = :usuarioId "
            + "and r.tipo = :tipo and r.data >= :inicio and r.data <= :fim group by r.subtipo")
    List<Object[]> somarPorSubtipo(@Param("usuarioId") Long usuarioId, @Param("tipo") RegistroGestao.Tipo tipo,
            @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("select r.subtipo, count(r.id) from RegistroGestao r where r.usuario.id = :usuarioId "
            + "and r.tipo = :tipo and r.data >= :inicio and r.data <= :fim group by r.subtipo")
    List<Object[]> contarPorSubtipo(@Param("usuarioId") Long usuarioId, @Param("tipo") RegistroGestao.Tipo tipo,
            @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    long countByUsuarioIdAndTipoAndConcluidaAndDataLessThan(Long usuarioId, RegistroGestao.Tipo tipo, Boolean concluida, LocalDate data);
    long countByUsuarioIdAndTipoAndConcluida(Long usuarioId, RegistroGestao.Tipo tipo, Boolean concluida);
}

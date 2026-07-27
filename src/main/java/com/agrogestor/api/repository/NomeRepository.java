package com.agrogestor.api.repository;

import com.agrogestor.api.model.Nome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NomeRepository extends JpaRepository<Nome, Long> {

}
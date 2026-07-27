package com.agrogestor.api.controller;

import com.agrogestor.api.dto.NomeRequestDTO;
import com.agrogestor.api.model.Nome;
import com.agrogestor.api.service.NomeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/nomes")
public class NomeController {


    private final NomeService nomeService;


    public NomeController(NomeService nomeService) {
        this.nomeService = nomeService;
    }


    @PostMapping
    public ResponseEntity<Nome> criar(
            @RequestBody NomeRequestDTO dto
    ){

        Nome nome = new Nome(dto.getNome());

        return ResponseEntity.ok(
                nomeService.salvar(nome)
        );
    }



    @GetMapping
    public ResponseEntity<List<Nome>> listar(){

        return ResponseEntity.ok(
                nomeService.listar()
        );
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id
    ){

        nomeService.deletar(id);

        return ResponseEntity.noContent().build();
    }

}
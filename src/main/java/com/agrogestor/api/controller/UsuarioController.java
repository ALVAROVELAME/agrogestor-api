package com.agrogestor.api.controller;

import com.agrogestor.api.dto.MensagemRespostaDTO;
import com.agrogestor.api.dto.UsuarioCadastroDTO;
import com.agrogestor.api.model.CadastroPendente;
import com.agrogestor.api.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {


    private final UsuarioService usuarioService;



    public UsuarioController(
            UsuarioService usuarioService
    ){

        this.usuarioService = usuarioService;

    }



    @PostMapping
    public ResponseEntity<MensagemRespostaDTO> criar(
            @RequestBody UsuarioCadastroDTO dto
    ){


        CadastroPendente cadastro =
                usuarioService.cadastrar(dto);



        return ResponseEntity.ok(

                new MensagemRespostaDTO(
                        true,
                        "Cadastro iniciado. Verifique seu email para confirmar a conta."
                )

        );

    }


}
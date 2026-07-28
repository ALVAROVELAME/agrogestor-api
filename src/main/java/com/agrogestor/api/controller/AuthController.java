package com.agrogestor.api.controller;

import com.agrogestor.api.dto.LoginDTO;
import com.agrogestor.api.dto.LoginRespostaDTO;
import com.agrogestor.api.service.AuthService;
import com.agrogestor.api.service.EmailConfirmationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EmailConfirmationService emailConfirmationService;

    private final AuthService authService;



    public AuthController(
            EmailConfirmationService emailConfirmationService,
            AuthService authService
    ){

        this.emailConfirmationService = emailConfirmationService;
        this.authService = authService;

    }



    @PostMapping("/login")
    public ResponseEntity<LoginRespostaDTO> login(
            @RequestBody LoginDTO dto
    ){

        boolean autenticado =
                authService.autenticar(dto);



        if(!autenticado){

            return ResponseEntity.badRequest()
                    .body(
                            new LoginRespostaDTO(
                                    "Email ou senha inválidos"
                            )
                    );

        }



        return ResponseEntity.ok(
                new LoginRespostaDTO(
                        "Login realizado com sucesso"
                )
        );

    }



    @GetMapping("/confirmar")
    public ResponseEntity<String> confirmarEmail(
            @RequestParam String token
    ){

        boolean confirmado =
                emailConfirmationService.confirmarEmail(token);



        if(confirmado){

            return ResponseEntity.ok(
                    "Email confirmado com sucesso"
            );

        }



        return ResponseEntity.badRequest()
                .body(
                        "Token inválido ou expirado"
                );

    }

}
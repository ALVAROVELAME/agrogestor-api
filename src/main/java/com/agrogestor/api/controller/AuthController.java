package com.agrogestor.api.controller;


import com.agrogestor.api.service.EmailConfirmationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
public class AuthController {



    private final EmailConfirmationService emailConfirmationService;



    public AuthController(
            EmailConfirmationService emailConfirmationService
    ){

        this.emailConfirmationService = emailConfirmationService;

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
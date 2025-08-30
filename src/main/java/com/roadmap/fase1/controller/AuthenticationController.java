package com.roadmap.fase1.controller;

import com.roadmap.fase1.dto.AuthenticationDTO;
import com.roadmap.fase1.dto.RegisterDTO;
import com.roadmap.fase1.model.User;
import com.roadmap.fase1.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
public class AuthenticationController {


    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Login do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autorizado (token JWT retornado)",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string",example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (usuário ou senha incorretos)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity login (@RequestBody  @Valid AuthenticationDTO data){
        String hasTokenAuth = userService.authenticate(data);
        if(hasTokenAuth.isBlank()){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(hasTokenAuth);
    }

    @PostMapping("/register")
    @Operation(summary = "Cria um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "409", description = "Email já registrado",
                    content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity register (@RequestBody  @Valid RegisterDTO data) throws InterruptedException {
        User newUser = userService.saveUser(data);
        if (newUser == null) { return  ResponseEntity.status(HttpStatus.CONFLICT).body("Email já registrado!");}
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}

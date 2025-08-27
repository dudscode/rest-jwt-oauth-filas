package com.roadmap.fase1.controller;

import com.roadmap.fase1.dto.AuthenticationDTO;
import com.roadmap.fase1.dto.RegisterDTO;
import com.roadmap.fase1.model.User;
import com.roadmap.fase1.service.UserService;
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
public class AuthenticationController {


    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity login (@RequestBody  @Valid AuthenticationDTO data){
        String hasTokenAuth = userService.authenticate(data);
        if(hasTokenAuth.isBlank()){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().body(hasTokenAuth);
    }

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody  @Valid RegisterDTO data){
        User newUser = userService.saveUser(data);
        if (newUser == null) { return  ResponseEntity.status(HttpStatus.CONFLICT).body("Email já registrado!");}
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}

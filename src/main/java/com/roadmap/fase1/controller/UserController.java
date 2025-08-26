package com.roadmap.fase1.controller;


import com.roadmap.fase1.dto.LoginRequest;
import com.roadmap.fase1.dto.NewUserResquest;
import com.roadmap.fase1.model.Roles;
import com.roadmap.fase1.model.User;
import com.roadmap.fase1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody NewUserResquest user) {
        User newUser = userService.saveUser(user);
        if (newUser == null) { return  ResponseEntity.status(HttpStatus.CONFLICT).body("Email já registrado!");}
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest user) {
        try {
            var loggedUser = userService.login(user);
            return ResponseEntity.ok(loggedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(@RequestBody String username) {
        Collection<Roles> roles = userService.getRoles(username);
        if (roles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/logged")
    public Object userInfo(
            @AuthenticationPrincipal OAuth2User principal,
            @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient) {

        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        return new Object() {
            public final Object user = principal.getAttributes();
            public final String token = accessToken;
        };
    }
}

package com.roadmap.fase1.service;

import com.roadmap.fase1.config.TokenProvider;
import com.roadmap.fase1.dto.LoginRequest;
import com.roadmap.fase1.dto.NewUserResquest;
import com.roadmap.fase1.dto.TokenResponse;
import com.roadmap.fase1.model.Roles;
import com.roadmap.fase1.model.User;
import com.roadmap.fase1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;


    public User saveUser(NewUserResquest user) {
        Optional<User> userExists = userRepository.findByEmail(user.getName());
        if (userExists.isPresent()) { return null; }
        return userRepository.save(User.builder()
                .identifier(UUID.randomUUID().toString())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(Collections.singletonList(roleService.getRoleByName(user.getRoleName())))
                .build());

    }

    public Boolean loginUser(LoginRequest user) {
        Optional<User> userLogin = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        return userLogin.isPresent();
    }

    public TokenResponse login(LoginRequest user) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
            return tokenProvider.generateToken(authentication);
        } catch (Exception e) {
            throw new Exception("Erro ao autenticar: " + e.getMessage());
        }
    }


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Collection<Roles> getRoles(String username) {
        return userRepository.findRolesByEmail(username) ;
    }
}

package com.roadmap.fase1.service;

import com.roadmap.fase1.config.TokenService;
import com.roadmap.fase1.dto.AuthenticationDTO;
import com.roadmap.fase1.dto.RegisterDTO;
import com.roadmap.fase1.model.User;
import com.roadmap.fase1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    public User saveUser(RegisterDTO user) {
        UserDetails userExists = userRepository.findByEmail(user.email());
        if (userExists != null) { return null; }
        String passwordEncrypted = new BCryptPasswordEncoder().encode(user.password());
        User newUser = new User(user.name(),passwordEncrypted, user.email(), user.phone(), user.role());
        return userRepository.save(newUser);
    }
    public String authenticate(AuthenticationDTO user) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(user.email(), user.password());
        var authentication = authenticationManager.authenticate(usernamePassword);
        return tokenService.generateToken((User) authentication.getPrincipal());
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}

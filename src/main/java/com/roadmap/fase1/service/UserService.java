package com.roadmap.fase1.service;

import com.roadmap.fase1.model.Login;
import com.roadmap.fase1.model.User;
import com.roadmap.fase1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        Optional<User> userExists = userRepository.findByEmail(user.getEmail());
        if (userExists.isPresent()) { return null; }
        return userRepository.save(user);
    }

    public Boolean loginUser(Login user) {
        Optional<User> userLogin = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        return userLogin.isPresent();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}

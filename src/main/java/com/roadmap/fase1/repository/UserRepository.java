package com.roadmap.fase1.repository;

import com.roadmap.fase1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;



public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByEmail(String email);
}

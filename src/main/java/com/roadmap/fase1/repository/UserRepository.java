package com.roadmap.fase1.repository;

import com.roadmap.fase1.model.Roles;
import com.roadmap.fase1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findByEmailAndPassword(String email, String password);
    @Query("SELECT r FROM User u JOIN u.roles r WHERE u.email = :email")
    Collection<Roles> findRolesByEmail(String email);
}

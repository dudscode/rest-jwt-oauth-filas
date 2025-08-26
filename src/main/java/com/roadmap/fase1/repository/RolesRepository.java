package com.roadmap.fase1.repository;

import com.roadmap.fase1.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RolesRepository  extends JpaRepository<Roles, String> {
    Optional<Roles> findByName(String name);
}

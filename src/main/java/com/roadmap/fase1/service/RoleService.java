package com.roadmap.fase1.service;

import com.roadmap.fase1.model.Roles;
import com.roadmap.fase1.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RolesRepository rolesRepository;

    public Roles getRoleByName(String name){
        Roles role = rolesRepository.findByName(name).orElse(null);
        if(Objects.isNull(role)){
            return rolesRepository.save(Roles.builder()
                    .identifier(UUID.randomUUID().toString())
                    .name("ROLE_" + name)
                    .build());
        }
        return role;
    }
}

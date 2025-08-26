package com.roadmap.fase1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "tb_roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Roles implements GrantedAuthority {
    @Id
    @Column(length = 36)
    private String identifier;
    private String  name;

    @Override
    public String getAuthority() {
        return name;
    }

}

package com.roadmap.fase1.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Entity
@Table(name = "tb_users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User  implements UserDetails {
    @Id
    @Column(length = 36)
    private String identifier;

    private String name;
    @Column(nullable = false)
    @ToString.Exclude
    private String password;
    @Column(nullable = false)
    private String email;
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_identifier"),
            inverseJoinColumns = @JoinColumn(name = "role_identifier"))
    @ToString.Exclude
    private Collection<Roles> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

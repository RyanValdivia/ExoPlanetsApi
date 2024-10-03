package com.pcn.exoplanets.models.user;

import com.pcn.exoplanets.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String email;
    private String imageUrl;

    private String provider;
    private String providerId;

    @Enumerated (EnumType.STRING)
    private Role role;

    @Override
    public boolean isEnabled () {
        return UserDetails.super.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired () {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isAccountNonLocked () {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isAccountNonExpired () {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public String getUsername () {
        return this.getEmail();
    }

    @Override
    public String getPassword () {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities () {
        return List.<GrantedAuthority>of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }
}

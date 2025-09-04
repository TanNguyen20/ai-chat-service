package com.tannguyen.ai.model.primary;

import com.tannguyen.ai.model.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "is_account_non_expired")
    private boolean isAccountNonExpired = true;

    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked = true;

    @Column(name = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired = true;

    @Column(name = "is_enabled")
    private boolean isEnabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
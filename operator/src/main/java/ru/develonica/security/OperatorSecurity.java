package ru.develonica.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.develonica.model.Operator;
import ru.develonica.model.mapper.OperatorMapper;

/**
 * Модель оператора для Spring Security
 * (реализация UserDetails).
 */
public class OperatorSecurity implements UserDetails, Operator {

    private final Long id;

    private final String email;

    private final String password;

    public OperatorSecurity(OperatorMapper operatorMapper) {
        this.id = operatorMapper.getId();
        this.email = operatorMapper.getEmail();
        this.password = operatorMapper.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    public Long getId() {
        return id;
    }
}

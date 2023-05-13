package ru.develonica.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.develonica.model.Operator;
import ru.develonica.model.mapper.OperatorMapper;
import ru.develonica.model.mapper.SpecializationMapper;

/**
 * Модель оператора для Spring Security
 * (реализация UserDetails).
 */
public class OperatorDetails implements UserDetails, Operator {

    /**
     * Идентификатор.
     */
    private final Long id;

    /**
     * Электронная почта.
     */
    private final String email;

    /**
     * Пароль.
     */
    private final String password;

    /**
     * Список специализаций, которые может обслуживать оператор.
     */
    private final List<SpecializationMapper> specializations;

    /**
     * Переключатель "активен ли оператор".
     */
    private boolean isActive;

    public OperatorDetails(OperatorMapper operatorMapper) {
        this.id = operatorMapper.getId();
        this.email = operatorMapper.getEmail();
        this.password = operatorMapper.getPassword();
        this.specializations = operatorMapper.getSpecializations();
        this.isActive = operatorMapper.isActive();
    }

    @Override
    public List<SpecializationMapper> getSpecializations() {
        return this.specializations;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
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
        return this.getEmail();
    }

    public String getEmail() {
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

    public void setActive(boolean value) {
        this.isActive = value;
    }
}

package ru.develonica.model.mapper;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Модель оператора.
 */
@Entity
@Table(name = "operator")
public class OperatorMapper {

    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operator_id")
    private Long id;

    /**
     * E-mail оператора.
     */
    @Column(name = "operator_email")
    private String email;

    /**
     * Пароль оператора.
     */
    @Column(name = "operator_password")
    private String password;

    /**
     * Имя оператора.
     */
    @Column(name = "operator_name")
    private String name;

    /**
     * UUID пользователя, с которым
     * в данный момент работает оператор.
     */
    @Column(name = "user_uuid")
    private UUID userUUID;

    /**
     * Флаг, который определяет активен
     * ли оператор в данный момент.
     */
    @Column(name = "operator_is_active")
    private boolean isActive;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

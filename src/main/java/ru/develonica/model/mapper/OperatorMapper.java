package ru.develonica.model.mapper;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
    @Getter
    private Long id;

    /**
     * E-mail оператора.
     */
    @Column(name = "operator_email")
    @Getter
    @Setter
    private String email;

    /**
     * Пароль оператора.
     */
    @Column(name = "operator_password")
    @Getter
    @Setter
    private String password;

    /**
     * Имя оператора.
     */
    @Column(name = "operator_name")
    @Getter
    @Setter
    private String name;

    /**
     * UUID пользователя, с которым
     * в данный момент работает оператор.
     */
    @Column(name = "user_uuid")
    @Getter
    @Setter
    private UUID userUUID;

    /**
     * Флаг, который определяет активен
     * ли оператор в данный момент.
     */
    @Column(name = "operator_is_active")
    @Getter
    @Setter
    private boolean isActive;
}

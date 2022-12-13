package ru.develonica.model.mapper;

import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import ru.develonica.model.Operator;

/**
 * Модель оператора.
 */
@Entity
@Table(name = "operator")
public class OperatorMapper implements Operator {

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

    @Column(name = "operator_is_active")
    private boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "operator_specialization",
            joinColumns = @JoinColumn(name = "operator_id"),
            inverseJoinColumns = @JoinColumn(name = "specialization_id")
    )
    List<SpecializationMapper> specializations;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public List<SpecializationMapper> getSpecializations() {
        return specializations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperatorMapper that = (OperatorMapper) o;

        if (!id.equals(that.id)) return false;
        if (!email.equals(that.email)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

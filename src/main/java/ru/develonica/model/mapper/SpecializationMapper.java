package ru.develonica.model.mapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

/**
 * Модель специализации.
 */
@Entity
@Table(name = "specialization")
public class SpecializationMapper {

    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialization_id")
    @Getter
    private Long id;

    /**
     * Наименование специализации.
     */
    @Column(name = "specialization_name")
    @Getter
    private String name;

    @Override
    public String toString() {
        return name;
    }
}

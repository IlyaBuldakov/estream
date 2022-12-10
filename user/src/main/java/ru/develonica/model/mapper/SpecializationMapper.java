package ru.develonica.model.mapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Модель специализации.
 */
@Entity
@Table(name = "specialization")
public class SpecializationMapper {

    public SpecializationMapper() {}

    public SpecializationMapper(String name) {
        this.name = name;
    }

    /**
     * Идентификатор.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialization_id")
    private Long id;

    /**
     * Наименование специализации.
     */
    @Column(name = "specialization_name")
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

package ru.develonica.model;

import java.util.List;
import ru.develonica.model.mapper.SpecializationMapper;

/**
 * Общий интерфейс для моделей оператора в приложении.
 * (представления для Spring Security и базовой сущности)
 */
public interface Operator {

    /**
     * Метод получения идентификатора.
     *
     * @return Идентификатор оператора.
     */
    Long getId();

    String getEmail();

    List<SpecializationMapper> getSpecializations();

    boolean isActive();

    void setActive(boolean value);
}
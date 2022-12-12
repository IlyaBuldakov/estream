package ru.develonica.model;

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
}
package ru.develonica.model.mapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Модель кода пользователя в очереди.
 */
@Entity
@Table(name = "code")
public class CodeMapper {

    public CodeMapper() {}

    public CodeMapper(char codeLetter, String sequenceName) {
        this.codeLetter = codeLetter;
        this.sequenceName = sequenceName;
    }

    /**
     * Идентификатор.
     */
    @Id
    @Column(name = "code_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Буква кода.
     */
    @Column(name = "code_letter")
    private char codeLetter;

    /**
     * Имя последовательности, из которой
     * берется числовое значение для кода.
     */
    @Column(name = "sequence_name")
    private String sequenceName;

    public String getSequenceName() {
        return sequenceName;
    }

    public char getCodeLetter() {
        return codeLetter;
    }
}

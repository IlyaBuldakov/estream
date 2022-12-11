package ru.develonica.model.mapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "code")
public class CodeMapper {

    public CodeMapper() {
    }

    public CodeMapper(char codeLetter, String sequenceName) {
        this.codeLetter = codeLetter;
        this.sequenceName = sequenceName;
    }

    @Id
    @Column(name = "code_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_letter")
    private char codeLetter;

    @Column(name = "sequence_name")
    private String sequenceName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public char getCodeLetter() {
        return codeLetter;
    }
}

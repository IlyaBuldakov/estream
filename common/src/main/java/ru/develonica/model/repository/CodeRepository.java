package ru.develonica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.CodeMapper;

public interface CodeRepository extends JpaRepository<CodeMapper, Long> {

    CodeMapper findBySequenceName(String sequenceName);
}

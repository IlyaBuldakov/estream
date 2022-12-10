package ru.develonica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.CodeMapper;

public interface CodeRepository extends JpaRepository<CodeMapper, Long> {
}

package ru.develonica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.OperatorMapper;

public interface OperatorRepository extends JpaRepository<OperatorMapper, Long> {

    OperatorMapper getByEmail(String email);
}
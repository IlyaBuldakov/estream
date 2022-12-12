package ru.develonica.model.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.OperatorMapper;
import ru.develonica.model.mapper.SpecializationMapper;

/**
 * Репозиторий для работы с операторами.
 */
public interface OperatorRepository extends JpaRepository<OperatorMapper, Long> {
    List<OperatorMapper> findAllBySpecializationsIn(List<SpecializationMapper> specializations);

    OperatorMapper getByEmail(String email);

}
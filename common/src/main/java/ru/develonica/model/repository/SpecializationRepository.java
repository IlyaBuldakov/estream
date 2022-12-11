package ru.develonica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.SpecializationMapper;

/**
 * Репозиторий для работы со специализациями.
 */
public interface SpecializationRepository extends JpaRepository<SpecializationMapper, Long> {

    /**
     * Метод поиска специализации по имени.
     *
     * @param name Имя специализации.
     * @return {@link SpecializationMapper}.
     */
    SpecializationMapper findByName(String name);
}

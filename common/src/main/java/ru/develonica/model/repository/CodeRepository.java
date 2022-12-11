package ru.develonica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.CodeMapper;

/**
 * Репозиторий для работы с кодом очереди.
 */
public interface CodeRepository extends JpaRepository<CodeMapper, Long> {

    /**
     * Метод поиска кода по имени последовательности.
     *
     * @param sequenceName Имя последовательности.
     * @return {@link CodeMapper}.
     */
    CodeMapper findBySequenceName(String sequenceName);
}

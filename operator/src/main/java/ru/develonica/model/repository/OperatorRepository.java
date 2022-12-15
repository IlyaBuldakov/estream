package ru.develonica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.OperatorMapper;

/**
 * Репозиторий для работы с операторами.
 */
public interface OperatorRepository extends JpaRepository<OperatorMapper, Long> {

    /**
     * Получение оператора по email.
     *
     * @param email Электронная почта.
     * @return {@link OperatorMapper Оператор}.
     */
    OperatorMapper getByEmail(String email);
}
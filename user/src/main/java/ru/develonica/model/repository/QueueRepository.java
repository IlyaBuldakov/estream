package ru.develonica.model.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.QueueMapper;

/**
 * Репозиторий для работы с очередями.
 */
public interface QueueRepository extends JpaRepository<QueueMapper, UUID> {
}

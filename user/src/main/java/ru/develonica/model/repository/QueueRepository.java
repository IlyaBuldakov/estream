package ru.develonica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.QueueMapper;

public interface QueueRepository extends JpaRepository<QueueMapper, Long> {
}

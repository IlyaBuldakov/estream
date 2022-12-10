package ru.develonica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.SpecializationMapper;

public interface SpecializationRepository extends JpaRepository<SpecializationMapper, Long> {
    SpecializationMapper findByName(String name);
}

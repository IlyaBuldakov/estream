package ru.develonica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.develonica.model.mapper.SpecializationMapper;

public interface SpecializationRepository extends JpaRepository<SpecializationMapper, Long> {
}

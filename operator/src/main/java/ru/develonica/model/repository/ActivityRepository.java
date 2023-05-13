package ru.develonica.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.develonica.model.mapper.ActivityMapper;

public interface ActivityRepository extends JpaRepository<ActivityMapper, Long> {

    ActivityMapper findByOperatorIdAndActivityFinishIsNull(long operatorId);
}

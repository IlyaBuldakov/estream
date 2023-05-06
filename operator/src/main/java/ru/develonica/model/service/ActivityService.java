package ru.develonica.model.service;

import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.ActivityMapper;
import ru.develonica.model.repository.ActivityRepository;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public void saveActivity(ActivityMapper activityMapper) {
        this.activityRepository.save(activityMapper);
    }

    public ActivityMapper findByActivityFinishIsNull() {
        return this.activityRepository.findByActivityFinishIsNull();
    }
}

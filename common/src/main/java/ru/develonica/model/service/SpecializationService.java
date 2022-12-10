package ru.develonica.model.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.SpecializationRepository;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public void createSpecialization(String specializationName) {
        specializationRepository.save(new SpecializationMapper(specializationName));
    }

    public List<SpecializationMapper> getSpecializations() {
        return specializationRepository.findAll();
    }
}

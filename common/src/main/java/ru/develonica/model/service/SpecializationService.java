package ru.develonica.model.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.SpecializationRepository;

/**
 * Сервис для работы со специализациями.
 */
@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    /**
     * Метод создания специализации.
     *
     * @param specializationName Имя специализации.
     */
    public void createSpecialization(String specializationName) {
        specializationRepository.save(new SpecializationMapper(specializationName));
    }

    /**
     * Метод получения всех специализаций.
     *
     * @return Список специализаций.
     */
    public List<SpecializationMapper> getSpecializations() {
        return specializationRepository.findAll();
    }

    /**
     * Метод удаления специализации по имени.
     *
     * @param specializationName Имя специализации.
     */
    public void deleteSpecialization(String specializationName) {
        SpecializationMapper specializationToDelete = specializationRepository
                .findByName(specializationName);
        specializationRepository.delete(specializationToDelete);
    }

    /**
     * Метод удаления всех специализаций.
     */
    public void deleteAllSpecializations() {
        specializationRepository.deleteAll();
    }
}

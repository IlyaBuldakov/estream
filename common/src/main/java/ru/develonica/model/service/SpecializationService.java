package ru.develonica.model.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.SpecializationRepository;

/**
 * Сервис для работы со специализациями.
 */
@Service
public class SpecializationService {

    private static final Logger LOG = LoggerFactory.getLogger(SpecializationService.class);

    private final SpecializationRepository specializationRepository;

    private final CodeService codeService;

    public SpecializationService(SpecializationRepository specializationRepository,
                                 CodeService codeService) {
        this.specializationRepository = specializationRepository;
        this.codeService = codeService;
    }

    /**
     * Метод создания специализации.
     *
     * @param specializationName Имя специализации.
     */
    public void createSpecialization(String specializationName) {
        this.specializationRepository.save(new SpecializationMapper(specializationName));
        LOG.info("Добавлена специализация %s".formatted(specializationName));
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
        SpecializationMapper specializationToDelete = this.specializationRepository
                .findByName(specializationName);
        LOG.info("Удалена специализация %s".formatted(specializationName));
        this.specializationRepository.delete(specializationToDelete);
    }

    /**
     * Метод удаления всех специализаций.
     */
    public void deleteAllSpecializations() {
        List<SpecializationMapper> allSpecializations = this.specializationRepository.findAll();
        for (SpecializationMapper specialization : allSpecializations) {
            this.codeService.dropSequence(specialization.getName());
        }
        this.specializationRepository.deleteAll();
    }
}

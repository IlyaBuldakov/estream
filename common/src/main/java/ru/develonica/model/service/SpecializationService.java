package ru.develonica.model.service;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.QueueMapper;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.SpecializationRepository;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    private final QueueService queueService;

    public SpecializationService(SpecializationRepository specializationRepository,
                                 QueueService queueService) {
        this.specializationRepository = specializationRepository;
        this.queueService = queueService;
    }

    public void createSpecialization(String specializationName) {
        specializationRepository.save(new SpecializationMapper(specializationName));
    }

    public List<SpecializationMapper> getSpecializations() {
        return specializationRepository.findAll();
    }

    public void deleteSpecialization(String specializationName) {
        SpecializationMapper specializationToDelete = specializationRepository
                .findByName(specializationName);
        specializationRepository.delete(specializationToDelete);
    }

    public void deleteAllSpecializations() {
        specializationRepository.deleteAll();
    }

    /**
     * Метод выбора специализации. Создает запись в таблице очереди
     * и возвращает код пользователя в очереди, исходя из выбранной специализации.
     *
     * @param activeSpecializationName Chosen specialization name.
     * @return Код пользователя в очереди.
     */
    public String chooseSpecialization(String activeSpecializationName) {
        QueueMapper queueEntry = queueService.createQueueEntry(activeSpecializationName);
        return queueEntry.getUserQueueCode();
    }
}

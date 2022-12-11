package ru.develonica.model.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.OperatorMapper;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.OperatorRepository;
import ru.develonica.security.OperatorSecurity;

/**
 * Сервис для работы с операторами.
 */
@Service
public class OperatorService {

    private final OperatorRepository operatorRepository;

    public OperatorService(OperatorRepository operatorRepository) {
        this.operatorRepository = operatorRepository;
    }

    /**
     * Добавить оператору специализацию.
     *
     * @param specialization Специализация.
     */
    public void addSpecialization(SpecializationMapper specialization) {
        OperatorMapper operatorMapper = operatorRepository.getByEmail(getCurrentOperatorEmail());
        operatorMapper.getSpecializations().add(specialization);
        this.operatorRepository.save(operatorMapper);
    }

    /**
     * Удалить у оператора специализацию.
     *
     * @param specialization Специализация.
     */
    public void deleteSpecialization(SpecializationMapper specialization) {
        OperatorMapper operatorMapper = operatorRepository.getByEmail(getCurrentOperatorEmail());
        operatorMapper.getSpecializations().remove(specialization);
        this.operatorRepository.save(operatorMapper);
    }

    /**
     * Получить email текущего авторизованного оператора.
     *
     * @return E-mail текущего авторизованного оператора.
     */
    private String getCurrentOperatorEmail() {
        return ((OperatorSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUsername();
    }
}
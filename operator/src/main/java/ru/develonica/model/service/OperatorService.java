package ru.develonica.model.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.develonica.model.Operator;
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

    public boolean isActiveById(long id) {
        Optional<OperatorMapper> operatorMapperOptional
                = this.operatorRepository.findById(id);
        if (operatorMapperOptional.isPresent()) {
            OperatorMapper operator = operatorMapperOptional.get();
            return operator.isActive();
        }
        return false;
    }

    public List<OperatorMapper> findBySpecializations(List<SpecializationMapper> specializations) {
        return this.operatorRepository.findAllBySpecializationsIn(specializations);
    }

    public OperatorMapper getByEmail(String email) {
        return this.operatorRepository.getByEmail(email);
    }

    public void setOperatorActive(Operator operator) {
        Optional<OperatorMapper> operatorMapperOptional
                = this.operatorRepository.findById(operator.getId());
        if (operatorMapperOptional.isPresent()) {
            OperatorMapper operatorMapper = operatorMapperOptional.get();
            operatorMapper.setActive(true);
            this.operatorRepository.save(operatorMapper);
        }
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

    /**
     * Метод установки UUID пользователя, которого обслуживает оператор.
     *
     * @param operator Оператор, чьё поле будем устанавливать.
     * @param currentUserUUID UUID пользователя.
     */
    public void setUserUuid(Operator operator, UUID currentUserUUID) {
        Optional<OperatorMapper> operatorOptional = operatorRepository.findById(operator.getId());
        if (operatorOptional.isPresent()) {
            OperatorMapper operatorFromDb = operatorOptional.get();
            operatorFromDb.setUserUUID(currentUserUUID);
            operatorRepository.save(operatorFromDb);
        }
    }
}
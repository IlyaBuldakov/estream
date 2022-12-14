package ru.develonica.model.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.develonica.model.Operator;
import ru.develonica.model.QueueEntryData;
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

    private final QueuePotentialPairHolder queuePotentialPairHolder;

    public OperatorService(OperatorRepository operatorRepository,
                           QueuePotentialPairHolder queuePotentialPairHolder) {
        this.operatorRepository = operatorRepository;
        this.queuePotentialPairHolder = queuePotentialPairHolder;
    }

    public Optional<QueueEntryData> getRequest(Operator currentOperator) {
        LinkedHashMap<QueueEntryData, Operator> pairMap
                = this.queuePotentialPairHolder.getMap();
        Set<Map.Entry<QueueEntryData, Operator>> entries = pairMap.entrySet();
        for (Map.Entry<QueueEntryData, Operator> entry : entries) {
            if (entry.getValue().equals(currentOperator)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
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
     * @param operator        Оператор, чьё поле будем устанавливать.
     * @param currentUserUUID UUID пользователя.
     */
    public void setUserUuid(Operator operator, Optional<UUID> currentUserUUID) {
        Optional<OperatorMapper> operatorOptional = operatorRepository.findById(operator.getId());
        if (operatorOptional.isPresent()) {
            OperatorMapper operatorFromDb = operatorOptional.get();
            if (currentUserUUID.isPresent()) {
                operatorFromDb.setUserUUID(currentUserUUID.get());
            } else {
                operatorFromDb.setUserUUID(null);
            }
            this.operatorRepository.save(operatorFromDb);
        }
    }

    public void acceptPair(QueueEntryData queueEntryData) {
        this.queuePotentialPairHolder.acceptPair(queueEntryData);
    }
}
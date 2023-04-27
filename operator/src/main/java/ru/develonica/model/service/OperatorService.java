package ru.develonica.model.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.develonica.model.Operator;
import ru.develonica.model.QueueEntryData;
import ru.develonica.model.SpecializationQueueEntryDataPair;
import ru.develonica.model.mapper.OperatorMapper;
import ru.develonica.model.mapper.QueueMapper;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.OperatorRepository;
import ru.develonica.model.repository.QueueRepository;
import ru.develonica.security.OperatorDetails;

/**
 * Сервис для работы с операторами.
 */
@Service
public class OperatorService {

    private final OperatorRepository operatorRepository;

    private final QueueRepository queueRepository;

    private final QueuePairHolder queuePairHolder;

    public OperatorService(OperatorRepository operatorRepository,
                           QueueRepository queueRepository,
                           QueuePairHolder queuePairHolder) {
        this.operatorRepository = operatorRepository;
        this.queueRepository = queueRepository;
        this.queuePairHolder = queuePairHolder;
    }

    /**
     * Метод получения запроса от пользователя из очереди.
     *
     * @param currentOperator Текущий оператор.
     * @return Информация о текущем пользователе и его выборе
     * (если таковой присутствует) в {@link Optional}.
     */
    public Optional<QueueEntryData> getRequestFromQueue(Operator currentOperator) {
        Queue<SpecializationQueueEntryDataPair> specMap
                = this.queuePairHolder.getWaitQueue();
        List<SpecializationMapper> operatorSpecializations = currentOperator.getSpecializations();
        for (int i = 0; i < specMap.size(); i++) {
            SpecializationQueueEntryDataPair peekedEntry = specMap.peek();
            if (operatorSpecializations.contains(peekedEntry.getSpecialization())) {
                return Optional.of(peekedEntry.getQueueEntryData());
            }
        }
        return Optional.empty();
    }

    /**
     * Метод установки переключателя "активен ли оператор".
     *
     * @param operator Оператор.
     * @param value    Новое значение "активен ли оператор".
     */
    public void setOperatorActive(Operator operator, boolean value) {
        Optional<OperatorMapper> operatorMapperOptional
                = this.operatorRepository.findById(operator.getId());
        if (operatorMapperOptional.isPresent()) {
            OperatorMapper operatorMapper = operatorMapperOptional.get();
            operatorMapper.setActive(value);
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
        return ((OperatorDetails) SecurityContextHolder
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

    /**
     * Метод принятия пользователя.
     *
     * @param queueEntryData  Информация о пользователе и его выборе.
     * @param currentOperator Текущий оператор.
     * @return Boolean - удалось ли принять.
     */
    public boolean acceptPair(QueueEntryData queueEntryData, Operator currentOperator) {
        return this.queuePairHolder.acceptPair(queueEntryData, currentOperator);
    }

    /**
     * Метод получения оператора по email.
     *
     * @param email Электронная почта.
     * @return {@link OperatorMapper Оператор}.
     */
    public OperatorMapper getByEmail(String email) {
        return this.operatorRepository.getByEmail(email);
    }

    /**
     * Метод получения статистики оператора по его идентификатору.
     *
     * @param operatorId Идентификатор оператора.
     * @return HashMap. Ключ - идентификатор специализации,
     * значение - количество обслуженных пользователей по этой специализации.
     */
    public HashMap<Long, Integer> getSpecializationStatsByOperatorId(long operatorId) {
        List<Long> specializationsServedByOperator = this.queueRepository
                .getQueueMappersByOperatorId(operatorId)
                .stream()
                .map(QueueMapper::getSpecializationId).toList();
        HashMap<Long, Integer> resultMap = new HashMap<>();

        for (Long specializationId : specializationsServedByOperator) {
            resultMap.put(
                    specializationId,
                    Collections.frequency(specializationsServedByOperator, specializationId)
            );
        }
        return resultMap;
    }
}
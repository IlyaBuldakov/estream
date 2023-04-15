package ru.develonica.model.service;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.develonica.model.Operator;
import ru.develonica.model.QueueEntryData;
import ru.develonica.model.SpecializationQueueEntryDataPair;

/**
 * Класс, хранящий в себе очередь записей в ожидании
 * одобрения (пары специализации и информации о пользователе)
 * и словаря принятых пар, а также методы для
 * взаимодействия с ними.
 */
@Component
public class QueuePairHolder {

    private static final Logger LOG = LoggerFactory.getLogger(QueuePairHolder.class);

    /**
     * Очередь записей в ожидании.
     */
    private final Queue<SpecializationQueueEntryDataPair> waitQueue
            = new LinkedBlockingQueue<>();

    /**
     * Словарь принятых пар (в которых оператор принял пользователя).
     */
    private final LinkedHashMap<QueueEntryData, Operator> acceptedPairs
            = new LinkedHashMap<>();

    /**
     * Добавить запись в очередь ожидания.
     *
     * @param queueEntryData Информация о пользователе и его выборе.
     */
    public void putPairInWaitQueue(QueueEntryData queueEntryData) {
        this.waitQueue.add(new SpecializationQueueEntryDataPair(
                queueEntryData.getSpecialization(),
                queueEntryData));
        LOG.info("В очередь добавлена запись. Пользователь: %s | Специализация: %s"
                .formatted(queueEntryData.getUserQueueCode(), queueEntryData.getSpecialization()));
    }

    /**
     * Принять пару (оператор принимает пользователя).
     * 1. Удаляет пару из очереди ожидания.
     * 2. Добавляет пару в словарь принятых пар
     *
     * @param queueEntryData  Информация о пользователе и его выборе.
     * @param currentOperator Текущий оператор.
     * @return Boolean - удалось ли принять пару.
     */
    public boolean acceptPair(QueueEntryData queueEntryData, Operator currentOperator) {
        if (this.waitQueue.remove(new SpecializationQueueEntryDataPair(queueEntryData.getSpecialization(), queueEntryData))) {
            this.acceptedPairs.put(queueEntryData, currentOperator);
            LOG.info("Пользователь %s принят оператором %s"
                    .formatted(queueEntryData.getUserQueueCode(), currentOperator.getId()));
            return true;
        }
        return false;
    }

    /**
     * Проверка, принят ли пользователь оператором
     *
     * @param queueEntryData Информация о пользователе и его выборе.
     * @return Оператор в {@link Optional}.
     */
    public Optional<Operator> checkAccept(QueueEntryData queueEntryData) {
        Operator operator = this.acceptedPairs.get(queueEntryData);
        if (operator != null) {
            this.acceptedPairs.remove(queueEntryData);
            return Optional.of(operator);
        }
        return Optional.empty();
    }

    /**
     * Метод получения очереди ожидания.
     *
     * @return Очередь ожидания.
     */
    public Queue<SpecializationQueueEntryDataPair> getWaitQueue() {
        return this.waitQueue;
    }
}

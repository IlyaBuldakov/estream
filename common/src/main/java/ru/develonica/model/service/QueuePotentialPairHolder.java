package ru.develonica.model.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.stereotype.Component;
import ru.develonica.model.Operator;
import ru.develonica.model.QueueEntryData;
import ru.develonica.model.SpecializationQueueEntryDataPair;

/**
 * Класс, хранящий в себе {@link HashMap словарь} с потенциальными
 * связками "оператор, uuid пользователя".
 * В потенциальной паре оператор обладает той специализацией,
 * по которой обращается пользователь из пары.
 */
@Component
public class QueuePotentialPairHolder {

    /**
     * Словарь потенциальных пар.
     */
    private final Queue<SpecializationQueueEntryDataPair> queue
            = new LinkedBlockingQueue<>();

    private final LinkedHashMap<QueueEntryData, Operator> acceptedPairs
            = new LinkedHashMap<>();

    public void putPair(QueueEntryData queueEntryData) {
        queue.add(new SpecializationQueueEntryDataPair(
                queueEntryData.getSpecialization(),
                queueEntryData));
    }

    public boolean acceptPair(QueueEntryData queueEntryData, Operator currentOperator) {
        if (queue.remove(new SpecializationQueueEntryDataPair(queueEntryData.getSpecialization(), queueEntryData))) {
            acceptedPairs.put(queueEntryData, currentOperator);
            return true;
        }
        return false;
    }

    public Optional<Operator> checkAccept(QueueEntryData queueEntryData) {
        Operator operator = acceptedPairs.get(queueEntryData);
        if (operator != null) {
            acceptedPairs.remove(queueEntryData);
            return Optional.of(operator);
        }
        return Optional.empty();
    }

    public Queue<SpecializationQueueEntryDataPair> getQueue() {
        return queue;
    }
}

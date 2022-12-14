package ru.develonica.model.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;
import ru.develonica.model.Operator;
import ru.develonica.model.QueueEntryData;

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
    private final LinkedHashMap<QueueEntryData, Operator> map
            = new LinkedHashMap<>();

    public void putPair(QueueEntryData queueEntryData, Operator operator) {
        map.put(queueEntryData, operator);
    }

    public void acceptPair(QueueEntryData queueEntryData) {
        Operator operator = map.get(queueEntryData);
        Set<Map.Entry<QueueEntryData, Operator>> entrySet = map.entrySet();
        for (Map.Entry<QueueEntryData, Operator> entry : entrySet) {
            QueueEntryData key = entry.getKey();
            if (key.equals(queueEntryData)) {
                key.setPairConnected(true);
                map.put(key, operator);
                break;
            }
        }
    }

    public Optional<Operator> checkAccept(QueueEntryData queueEntryData) {
        Set<Map.Entry<QueueEntryData, Operator>> entrySet = map.entrySet();
        for (Map.Entry<QueueEntryData, Operator> entry : entrySet) {
            QueueEntryData key = entry.getKey();
            if (key.equals(queueEntryData)) {
                if (key.isPairConnected()) {
                    return Optional.of(entry.getValue());
                }
            }
        }
        return Optional.empty();
    }

    public LinkedHashMap<QueueEntryData, Operator> getMap() {
        return map;
    }
}

package ru.develonica.model.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import org.springframework.stereotype.Component;
import ru.develonica.model.mapper.OperatorMapper;

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
    private final LinkedHashMap<UUID, OperatorMapper> map = new LinkedHashMap<>();

    public void putPair(UUID currentUserUUID, OperatorMapper operator) {
        map.put(currentUserUUID, operator);
    }

    public void removePair(UUID currentUserUUID) {
        map.remove(currentUserUUID);
    }

    public LinkedHashMap<UUID, OperatorMapper> getMap() {
        return map;
    }
}

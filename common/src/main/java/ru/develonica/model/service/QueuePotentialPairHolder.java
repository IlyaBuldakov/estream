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
    private final LinkedHashMap<OperatorMapper, UUID> map = new LinkedHashMap<>();

    public void put(OperatorMapper operator, UUID currentUserUUID) {
        map.put(operator, currentUserUUID);
    }

    public LinkedHashMap<OperatorMapper, UUID> getMap() {
        return map;
    }
}

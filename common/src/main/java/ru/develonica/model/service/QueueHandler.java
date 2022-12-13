package ru.develonica.model.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.OperatorMapper;
import ru.develonica.model.mapper.SpecializationMapper;

/**
 * Класс отвечающий работу в очереди.
 */
@Service
public class QueueHandler {

    private final QueuePotentialPairHolder queuePotentialPairHolder;

    private final OperatorService operatorService;

    private UUID currentUserUUID;

    private SpecializationMapper currentSpecialization;

    private boolean operatorAcceptedCurrentUser;

    public QueueHandler(QueuePotentialPairHolder queuePotentialPairHolder,
                        OperatorService operatorService) {
        this.queuePotentialPairHolder = queuePotentialPairHolder;
        this.operatorService = operatorService;
    }

    /**
     * Запуск цикла пользователя. Ищет всех возможных операторов,
     * которые обладают нужной специализацией и отправляет им запрос на обслуживание.
     *
     * @param specialization Специализация, по которой обращается пользователь.
     */
    public void sendRequests(SpecializationMapper specialization) {
        List<OperatorMapper> operatorList = this.operatorService
                .findBySpecializations(List.of(specialization));
        boolean requestsSend = false;
        while (!requestsSend) {
            if (operatorList.isEmpty()) {
                continue;
            }
            List<CompletableFuture<Void>> cfList = new ArrayList<>();
            for (OperatorMapper operator : operatorList) {
                cfList.add(CompletableFuture.supplyAsync(() -> {
                    this.queuePotentialPairHolder.putPair(currentUserUUID, operator);
                    return null;
                }));
                CompletableFuture.allOf(cfList.toArray(CompletableFuture[]::new)).join();
                requestsSend = true;
            }
        }
    }

    /**
     * Метод выхода пользователя из очереди.
     */
    public void userLeaveQueue() {
        this.queuePotentialPairHolder.removePair(this.currentUserUUID);
        this.currentSpecialization = null;
        this.setOperatorAcceptedCurrentUser(false);
    }

    /**
     * Запуск цикла оператора. Ищет в словаре потенциальных пар пользователя,
     * которого он может обслужить (для этого в словаре потенциальных пар
     * должна быть запись с ключом - оператор, значением - uuid пользователя).
     *
     * @param currentOperator Текущий оператор.
     * @return Boolean - есть ли пользователь, которого нужно обслужить.
     */
    public boolean startOperatorLoop(OperatorMapper currentOperator) {
        LinkedHashMap<UUID, OperatorMapper> pairMap = this.queuePotentialPairHolder.getMap();
        while (true) {
            if (pairMap.containsValue(currentOperator)) {
                return true;
            }
        }
    }

    public void setCurrentUserUUID(UUID currentUserUUID) {
        this.currentUserUUID = currentUserUUID;
    }

    public SpecializationMapper getCurrentSpecialization() {
        return currentSpecialization;
    }

    public void setCurrentSpecialization(SpecializationMapper currentSpecialization) {
        this.currentSpecialization = currentSpecialization;
    }

    public UUID getCurrentUserUUID() {
        return currentUserUUID;
    }

    public boolean isOperatorAcceptedCurrentUser() {
        return operatorAcceptedCurrentUser;
    }

    public void setOperatorAcceptedCurrentUser(boolean operatorAcceptedCurrentUser) {
        this.operatorAcceptedCurrentUser = operatorAcceptedCurrentUser;
    }
}

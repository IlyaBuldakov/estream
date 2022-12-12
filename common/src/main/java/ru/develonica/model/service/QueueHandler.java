package ru.develonica.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.OperatorMapper;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.OperatorRepository;

/**
 * Класс отвечающий работу в очереди.
 */
@Service
public class QueueHandler {

    private final QueuePotentialPairHolder queuePotentialPairHolder;

    private final OperatorRepository operatorRepository;

    private UUID currentUserUUID;

    public QueueHandler(QueuePotentialPairHolder queuePotentialPairHolder, OperatorRepository operatorRepository) {
        this.queuePotentialPairHolder = queuePotentialPairHolder;
        this.operatorRepository = operatorRepository;
    }

    /**
     * Запуск цикла пользователя. Ищет всех возможных операторов,
     * которые обладают нужной специализацией и отправляет им запрос на обслуживание.
     *
     * @param specialization Специализация, по которой обращается пользователь.
     */
    public void startUserLoop(SpecializationMapper specialization) {
        CompletableFuture.supplyAsync(() -> {
            boolean operatorFound = false;
            List<OperatorMapper> operatorList = operatorRepository
                    .findAllBySpecializationsIn(List.of(specialization));
            while (!operatorFound) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (operatorList.isEmpty()) {
                    continue;
                }
                List<CompletableFuture<Void>> cfList = new ArrayList<>();
                for (OperatorMapper operator : operatorList) {
                    cfList.add(CompletableFuture.supplyAsync(() -> {
                        this.queuePotentialPairHolder.putPair(operator, currentUserUUID);
                        return null;
                    }));
                    CompletableFuture.allOf(cfList.toArray(CompletableFuture[]::new)).join();
                }
            }
            return null;
        });
    }

    /**
     * Метод выхода пользователя из очереди.
     */
    public void userLeaveQueue() {
        this.queuePotentialPairHolder.removePair(this.currentUserUUID);
    }

    /**
     * Запуск цикла оператора. Ищет в словаре потенциальных пар пользователя,
     * которого он может обслужить (для этого в словаре потенциальных пар
     * должна быть запись с ключом - оператор, значением - uuid пользователя.
     *
     * @param currentOperator Текущий оператор.
     * @return Boolean - найден ли пользователь, которого нужно обслужить.
     */
    public boolean startOperatorLoop(OperatorMapper currentOperator) {
        while (true) {
            if (this.queuePotentialPairHolder.getMap().containsKey(currentOperator)) {
                return true;
            }
        }
    }

    public void setCurrentUserUUID(UUID currentUserUUID) {
        this.currentUserUUID = currentUserUUID;
    }
}
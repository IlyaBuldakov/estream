package ru.develonica.model.service;

import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.develonica.security.OperatorSecurity;

/**
 * Сервис действий с очередью.
 */
@Service
public class QueueActionService {

    private final QueueService queueService;

    private final OperatorService operatorService;

    public QueueActionService(QueueService queueService, OperatorService operatorService) {
        this.queueService = queueService;
        this.operatorService = operatorService;
    }

    /**
     * Метод принятия пользователя в очереди.
     *
     * @param currentUserUuid UUID пользователя.
     */
    public void accept(UUID currentUserUuid) {
        OperatorSecurity operator = (OperatorSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        queueService.setOperator(currentUserUuid, operator);
        operatorService.setUserUuid(operator, currentUserUuid);
    }
}

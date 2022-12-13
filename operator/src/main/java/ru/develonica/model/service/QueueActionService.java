package ru.develonica.model.service;

import java.sql.Timestamp;
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
        this.queueService.setOperator(currentUserUuid, operator);
        this.operatorService.setUserUuid(operator, currentUserUuid);
    }

    /**
     * Метод установки даты начала обслуживания.
     *
     * @param currentUserUuid UUID пользователя (идентификатор очереди).
     * @param dateStart Дата начала обслуживания.
     */
    public void setDateStart(UUID currentUserUuid, Timestamp dateStart) {
        this.queueService.setDateStart(currentUserUuid, dateStart);
    }
}

package ru.develonica.controller;

import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.develonica.model.service.OperatorService;
import ru.develonica.model.service.QueueHandler;
import ru.develonica.model.service.QueueService;
import ru.develonica.security.OperatorSecurity;

/**
 * Контроллер для работы с очередью.
 */
@Controller
@RequestMapping("/queue")
public class QueueController {

    private final QueueService queueService;

    private final QueueHandler queueHandler;

    private final OperatorService operatorService;

    public QueueController(QueueService queueService,
                           QueueHandler queueHandler,
                           OperatorService operatorService) {
        this.queueService = queueService;
        this.queueHandler = queueHandler;
        this.operatorService = operatorService;
    }

    /**
     * Принятие пользователя оператором.
     *
     * @return Представление с чатом.
     */
    @GetMapping("/accept")
    public String acceptUser() {
        OperatorSecurity operator = (OperatorSecurity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        UUID currentUserUUID = this.queueHandler.getCurrentUserUUID();
        queueService.setOperator(currentUserUUID, operator);
        operatorService.setUserUuid(operator, currentUserUUID);
        return "redirect:/chat";
    }
}
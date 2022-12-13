package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.develonica.model.service.QueueActionService;
import ru.develonica.model.service.QueueHandler;

/**
 * Контроллер для работы с очередью.
 */
@Controller
@RequestMapping("/queue")
public class QueueController {

    private final QueueHandler queueHandler;

    private final QueueActionService queueActionService;

    public QueueController(QueueHandler queueHandler,
                           QueueActionService queueActionService) {
        this.queueHandler = queueHandler;
        this.queueActionService = queueActionService;
    }

    /**
     * Принятие пользователя оператором.
     *
     * @return Представление с чатом.
     */
    @GetMapping("/accept")
    public String acceptUser() {
        queueActionService.accept(this.queueHandler.getCurrentUserUUID());
        return "redirect:/chat";
    }
}
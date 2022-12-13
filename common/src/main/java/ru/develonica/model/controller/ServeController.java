package ru.develonica.model.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.develonica.model.service.ChatService;
import ru.develonica.model.service.QueueActionService;
import ru.develonica.model.service.QueueHandler;
import ru.develonica.model.ui.PanelUiController;

@Controller
@RequestMapping("/serve")
public class ServeController {

    private final ChatService chatService;

    private final QueueHandler queueHandler;

    private final QueueActionService queueActionService;

    private final PanelUiController panelUiController;

    public ServeController(ChatService chatService,
                           QueueHandler queueHandler,
                           QueueActionService queueActionService,
                           PanelUiController panelUiController) {
        this.chatService = chatService;
        this.queueHandler = queueHandler;
        this.queueActionService = queueActionService;
        this.panelUiController = panelUiController;
    }

    /**
     * Принятие пользователя оператором.
     *
     * @return Представление с чатом.
     */
    @GetMapping("/accept")
    public String acceptUser(@RequestParam String chatGreeting) {
        UUID currentUserUUID = this.queueHandler.getCurrentUserUUID();
        this.queueActionService.accept(currentUserUUID);
        this.queueActionService.setDateStart(currentUserUUID, Timestamp.from(Instant.now()));
        this.queueHandler.setOperatorAcceptedCurrentUser(true);
        this.panelUiController.thereIsUserToServe(false);
        this.chatService.setChatGreeting(chatGreeting);
        return "redirect:/chat.xhtml";
    }

    /**
     * Получение представления чата.
     */
    @GetMapping("/enter")
    public String getChat() {
        return "redirect:/chat.xhtml";
    }
}

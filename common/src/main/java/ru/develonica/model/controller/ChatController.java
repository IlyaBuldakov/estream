package ru.develonica.model.controller;

import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.develonica.model.service.ChatService;
import ru.develonica.model.service.QueueActionService;
import ru.develonica.model.service.QueueHandler;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    private final QueueHandler queueHandler;

    private final QueueActionService queueActionService;

    public ChatController(ChatService chatService,
                          QueueHandler queueHandler,
                          QueueActionService queueActionService) {
        this.chatService = chatService;
        this.queueHandler = queueHandler;
        this.queueActionService = queueActionService;
    }

    /**
     * Принятие пользователя оператором.
     *
     * @return Представление с чатом.
     */
    @GetMapping("/accept")
    public String acceptUser(@RequestParam String chatGreeting) {
        this.queueActionService.accept(this.queueHandler.getCurrentUserUUID());
        this.queueHandler.setOperatorAcceptedCurrentUser(true);
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

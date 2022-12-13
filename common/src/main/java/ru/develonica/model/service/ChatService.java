package ru.develonica.model.service;

import org.springframework.stereotype.Service;
import ru.develonica.model.ui.ChatUiController;

/**
 * Сервис для работы с чатом.
 */
@Service
public class ChatService {

    private final ChatUiController chatUiController;

    public ChatService(ChatUiController chatUiController) {
        this.chatUiController = chatUiController;
    }

    /**
     * Установка приветствия в чате.
     *
     * @param chatGreeting Приветствие в чате.
     */
    public void setChatGreeting(String chatGreeting) {
        this.chatUiController.setChatGreeting(chatGreeting);
    }
}

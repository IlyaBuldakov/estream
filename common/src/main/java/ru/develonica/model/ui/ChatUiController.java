package ru.develonica.model.ui;

import javax.faces.bean.ManagedBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@ManagedBean(name = "chatUiController")
@SessionScope
@Component
public class ChatUiController {

    private String chatGreeting = "Чат с оператором";

    public String getChatGreeting() {
        return chatGreeting;
    }

    public void setChatGreeting(String chatGreeting) {
        this.chatGreeting = chatGreeting;
    }
}

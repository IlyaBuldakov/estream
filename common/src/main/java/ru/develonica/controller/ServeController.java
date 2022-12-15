package ru.develonica.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.develonica.model.Operator;
import ru.develonica.controller.ui.ServeUiController;

@Controller
@RequestMapping("/serve")
public class ServeController {

    private final ServeUiController serveUiController;

    public ServeController(ServeUiController serveUiController) {
        this.serveUiController = serveUiController;
    }

    @GetMapping
    public String getChat() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Operator) {
            this.serveUiController.setIsOperator(true);
        }
        return "chat.xhtml";
    }
}

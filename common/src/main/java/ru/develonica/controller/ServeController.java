package ru.develonica.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.develonica.model.managed.ServeBean;
import ru.develonica.model.Operator;

@Controller
@RequestMapping("/serve")
public class ServeController {

    private final ServeBean serveBean;

    public ServeController(ServeBean serveBean) {
        this.serveBean = serveBean;
    }

    @GetMapping
    public String getChat() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Operator) {
            this.serveBean.setIsOperator(true);
        }
        return "chat.xhtml";
    }
}

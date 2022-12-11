package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер авторизации.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * Получение представления для авторизации
     * (страница с формой входа).
     *
     * @return Представление для авторизации.
     */
    @GetMapping
    public String getLogin() {
        return "login.xhtml";
    }
}

package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контроллер статистики.
 */
@Controller
@RequestMapping("/stat")
public class StatController {

    /**
     * Метод получения страницы статистики.
     * @return Страница статистики.
     */
    @GetMapping
    public String getStat() {
        return "stat.xhtml?i=2";
    }
}

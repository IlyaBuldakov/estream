package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.develonica.controller.ui.SpecializationBean;

/**
 * Welcome page контроллер.
 */
@Controller
@RequestMapping("/")
public class UserGreetingController {

    private final SpecializationBean specializationBean;

    public UserGreetingController(SpecializationBean specializationBean) {
        this.specializationBean = specializationBean;
    }

    /**
     * Получение welcome page (страницы специализаций).
     *
     * @return Представление специализаций.
     */
    @GetMapping
    public String getGreetingPage() {
        this.specializationBean.resetSessionInfo();
        return "specializations.xhtml";
    }
}
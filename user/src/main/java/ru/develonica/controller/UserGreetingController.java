package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.develonica.controller.ui.SpecializationUiController;

/**
 * Welcome page контроллер.
 */
@Controller
@RequestMapping("/")
public class UserGreetingController {

    private final SpecializationUiController specializationUiController;

    public UserGreetingController(SpecializationUiController specializationUiController) {
        this.specializationUiController = specializationUiController;
    }

    /**
     * Получение welcome page (страницы специализаций).
     *
     * @return Представление специализаций.
     */
    @GetMapping
    public String getGreetingPage() {
        this.specializationUiController.resetSessionInfo();
        return "specializations.xhtml";
    }
}
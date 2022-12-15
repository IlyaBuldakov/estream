package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.develonica.model.ui.SpecializationUiController;

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

    @GetMapping
    public String getGreetingPage() {
        this.specializationUiController.resetSessionInfo();
        return "specializations.xhtml";
    }
}
package ru.develonica.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.develonica.model.Operator;
import ru.develonica.model.service.CodeService;
import ru.develonica.model.service.SpecializationService;
import ru.develonica.controller.ui.PanelUiController;

/**
 * Контроллер панели управления.
 */
@Controller
@RequestMapping("/panel")
public class PanelController {

    private final SpecializationService specializationService;

    private final PanelUiController panelUiController;

    private final CodeService codeService;

    public PanelController(SpecializationService specializationService,
                           PanelUiController panelUiController,
                           CodeService codeService) {
        this.specializationService = specializationService;
        this.panelUiController = panelUiController;
        this.codeService = codeService;
    }

    /**
     * Получение представления с панелью.
     *
     * @return Представление с панелью.
     */
    @GetMapping
    public String getPanel() {
        Operator currentOperator =
                (Operator) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        this.panelUiController.setOperator(currentOperator);
        this.panelUiController.resetSessionInfo();
        return "panel.xhtml";
    }

    /*
     ******* Действия со специализациями *******
     */

    /**
     * Создание специализации.
     *
     * @param specializationName Имя специализации.
     * @return Представление с панелью.
     */
    @PostMapping("/create-specialization")
    public String createSpecialization(@RequestParam String specializationName) {
        this.specializationService.createSpecialization(specializationName);
        this.codeService.generateCodeEntry(specializationName);
        return "redirect:/panel";
    }

    /**
     * Удаление специализации по имени.
     *
     * @param specializationName Имя специализации.
     * @return Представление с панелью.
     */
    @PostMapping("/delete-specialization")
    public String deleteSpecialization(@RequestParam String specializationName) {
        this.specializationService.deleteSpecialization(specializationName);
        this.codeService.deleteCodeEntry(specializationName);
        return "redirect:/panel";
    }

    /**
     * Удаление всех специализаций.
     *
     * @return Представление с панелью.
     */
    @PostMapping("/delete-all-specializations")
    public String deleteAllSpecializations() {
        this.specializationService.deleteAllSpecializations();
        return "redirect:/panel";
    }
}
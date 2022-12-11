package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.develonica.model.service.CodeService;
import ru.develonica.model.service.SpecializationService;

/**
 * Контроллер панели управления.
 */
@Controller
@RequestMapping("/panel")
public class PanelController {

    private final SpecializationService specializationService;

    private final CodeService codeService;

    public PanelController(SpecializationService specializationService,
                           CodeService codeService) {
        this.specializationService = specializationService;
        this.codeService = codeService;
    }

    /**
     * Получение представления с панелью.
     *
     * @return Представление с панелью.
     */
    @GetMapping
    public String getGreetingPage() {
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
        specializationService.createSpecialization(specializationName);
        codeService.generateCodeEntry(specializationName);
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
        specializationService.deleteSpecialization(specializationName);
        return "redirect:/panel";
    }

    /**
     * Удаление всех специализаций.
     *
     * @return Представление с панелью.
     */
    @PostMapping("/delete-all-specializations")
    public String deleteAllSpecializations() {
        specializationService.deleteAllSpecializations();
        return "redirect:/panel";
    }
}
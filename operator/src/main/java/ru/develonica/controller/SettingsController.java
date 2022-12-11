package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.SpecializationRepository;
import ru.develonica.model.service.OperatorService;

/**
 * Контроллер для работы с настройкой (для операторов).
 */
@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final OperatorService operatorService;

    private final SpecializationRepository specializationRepository;

    public SettingsController(OperatorService operatorService,
                              SpecializationRepository specializationRepository) {
        this.operatorService = operatorService;
        this.specializationRepository = specializationRepository;
    }

    /**
     * Добавление специализации оператору.
     *
     * @param specializationName Имя специализации.
     * @return Представление с панелью.
     */
    @PostMapping("/operator-add-specialization")
    public String addSpecializationToOperator(@RequestParam String specializationName) {
        SpecializationMapper specialization = this.specializationRepository
                .findByName(specializationName);
        this.operatorService.addSpecialization(specialization);
        return "redirect:/panel";
    }

    /**
     * Удаление специализации у оператора.
     *
     * @param specializationName Имя специализации.
     * @return Представление с панелью.
     */
    @PostMapping("/operator-delete-specialization")
    public String deleteSpecializationFromOperator(@RequestParam String specializationName) {
        SpecializationMapper specialization = this.specializationRepository
                .findByName(specializationName);
        this.operatorService.deleteSpecialization(specialization);
        return "redirect:/panel";
    }
}

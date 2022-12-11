package ru.develonica.controller;

import java.sql.SQLException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.develonica.model.service.SpecializationService;
import ru.develonica.model.service.CodeService;

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

    @GetMapping
    public String getGreetingPage() {
        return "panel.xhtml";
    }

    @PostMapping("/create-specialization")
    public String createSpecialization(@RequestParam String specializationName)
            throws SQLException {
        specializationService.createSpecialization(specializationName);
        codeService.generateCode(specializationName);
        return "redirect:/panel";
    }

    @PostMapping("/delete-specialization")
    public String deleteSpecialization(@RequestParam String specializationName) {
        specializationService.deleteSpecialization(specializationName);
        return "redirect:/panel";
    }

    @PostMapping("/delete-all-specializations")
    public String deleteAllSpecializations() {
        specializationService.deleteAllSpecializations();
        return "redirect:/panel";
    }
}
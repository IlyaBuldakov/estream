package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.develonica.model.service.SpecializationService;

@Controller
@RequestMapping("/panel")
public class PanelController {

    private final SpecializationService specializationService;

    public PanelController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping
    public String getGreetingPage() {
        return "panel.xhtml";
    }

    @PostMapping("/create-specialization")
    public String createSpecialization(@RequestParam String specializationName) {
        specializationService.createSpecialization(specializationName);
        return "redirect:/panel";
    }
}
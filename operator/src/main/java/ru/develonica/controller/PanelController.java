package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.SpecializationRepository;

@Controller
@RequestMapping("/panel")
public class PanelController {

    private final SpecializationRepository specializationRepository;

    public PanelController(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    @GetMapping
    public String getGreetingPage() {
        return "panel.xhtml";
    }

    @PostMapping("/create-specialization")
    public String createSpecialization(@RequestParam String specializationName) {
        specializationRepository.save(new SpecializationMapper(specializationName));
        return "redirect:/panel";
    }
}
package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class GreetingPageController {

    @GetMapping
    public String getGreetingPage() {
        return "specializations.xhtml";
    }
}
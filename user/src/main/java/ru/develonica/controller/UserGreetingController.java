package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Welcome page контроллер.
 */
@Controller
@RequestMapping("/")
public class UserGreetingController {

    @GetMapping
    public String getGreetingPage() {
        return "specializations.xhtml";
    }
}
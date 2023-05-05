package ru.develonica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/serve")
public class ServeController {

    @GetMapping
    public String getChat() {
        return "chat.xhtml";
    }
}

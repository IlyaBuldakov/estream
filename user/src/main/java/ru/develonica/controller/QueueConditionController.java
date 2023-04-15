package ru.develonica.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.develonica.controller.ui.SpecializationBean;

/**
 * Контроллер для получения информации о состоянии очереди.
 */
@RestController
public class QueueConditionController {

    /**
     * Managed bean специализаций.
     */
    public SpecializationBean specializationBean;

    public QueueConditionController(SpecializationBean specializationBean) {
        this.specializationBean = specializationBean;
    }

    /**
     * Метод проверки "принят ли пользователь (оператором).
     * @return JSON. accepted:true/false
     */
    @GetMapping("/checkAccepted")
    public String isUserAccepted() {
        return this.specializationBean.getAcceptedAsJson();
    }
}
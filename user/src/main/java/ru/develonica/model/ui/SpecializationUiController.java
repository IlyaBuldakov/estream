package ru.develonica.model.ui;

import java.util.List;
import javax.faces.bean.ManagedBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.develonica.model.mapper.QueueMapper;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.service.QueueHandler;
import ru.develonica.model.service.SpecializationService;

/**
 * UI-контроллер специализаций.
 * Контроллер представления специализаций.
 */
@ManagedBean(name = "specializationUiController")
@SessionScope
@Component
public class SpecializationUiController extends AbstractUiController {

    private final SpecializationService specializationService;

    private final QueueHandler queueHandler;

    private boolean specializationChosen;

    private SpecializationMapper activeSpecialization;

    private String userQueueCode;

    public SpecializationUiController(SpecializationService specializationService,
                                      QueueHandler queueHandler) {
        this.specializationService = specializationService;
        this.queueHandler = queueHandler;
    }

    /**
     * Получение всех специализаций.
     *
     * @return Список специализаций.
     */
    public List<SpecializationMapper> getSpecializations() {
        return specializationService.getSpecializations();
    }

    /**
     * Выбор специализации.
     *
     * @param specialization Выбранная специализация.
     * @return Представление со специализациями.
     */
    public String chooseSpecialization(SpecializationMapper specialization) {
        this.specializationChosen = true;
        this.activeSpecialization = specialization;
        QueueMapper createdQueueMapper = this.specializationService
                .chooseSpecialization(this.activeSpecialization.toString());
        this.userQueueCode = createdQueueMapper.getUserQueueCode();
        this.queueHandler.setCurrentUserUUID(createdQueueMapper.getId());
        this.queueHandler.setCurrentSpecialization(this.activeSpecialization);
        return "specializations.xhtml";
    }

    /**
     * Отменить выбор специализации.
     *
     * @return Представление со специализациями.
     */
    public String cancelChoose() {
        this.specializationChosen = false;
        this.activeSpecialization = null;
        this.queueHandler.userLeaveQueue();
        return "specializations.xhtml";
    }

    /**
     * Метод начала цикла очереди.
     */
    public void startQueueLoop() {
        while (!this.queueHandler.isOperatorAcceptedCurrentUser()) {
            this.queueHandler.sendRequests(this.activeSpecialization);
        }
        super.redirect("serve/enter");
    }

    /**
     * Метод - проверка параметра "выбрана ли специализация (пользователем)".
     *
     * @return Boolean - выбрана ли специализация (пользователем).
     */
    public boolean isSpecializationChosen() {
        return specializationChosen;
    }

    /**
     * Получение имени активной (выбранной) специализации.
     *
     * @return Имя выбранной специализации.
     */
    public String getActiveSpecializationName() {
        return activeSpecialization.toString();
    }

    /**
     * Получение кода пользователя в очереди.
     *
     * @return Код пользователя в очереди.
     */
    public String getUserQueueCode() {
        return userQueueCode;
    }
}
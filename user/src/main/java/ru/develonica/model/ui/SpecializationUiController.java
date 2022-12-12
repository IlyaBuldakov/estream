package ru.develonica.model.ui;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.springframework.stereotype.Component;
import ru.develonica.model.mapper.QueueMapper;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.service.QueueHandler;
import ru.develonica.model.service.SpecializationService;

/**
 * UI-контроллер специализаций.
 * Контроллер представления специализаций.
 */
@ManagedBean(name = "specializationUiController")
@SessionScoped
@Component
public class SpecializationUiController implements Serializable {

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
        this.queueHandler.startUserLoop(this.activeSpecialization);
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
package ru.develonica.controller.ui;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.develonica.model.Operator;
import ru.develonica.model.QueueEntryData;
import ru.develonica.model.mapper.QueueMapper;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.service.CodeService;
import ru.develonica.model.service.QueueService;
import ru.develonica.model.service.SpecializationService;

/**
 * UI контроллер для обработки ввода на странице специализаций.
 */
@ManagedBean(name = "specializationUiController")
@SessionScope
@Component
public class SpecializationUiController extends AbstractUiController {

    private final QueueService queueService;

    private final SpecializationService specializationService;

    private final CodeService codeService;

    /**
     * Переключатель "выбрана ли специализация".
     */
    private boolean specializationChosen;

    /**
     * Переключатель "зарегистрирована ли очередь"
     * (создана запись в таблице, сгенерирован объект {@link QueueMapper}.
     */
    private boolean queueRegistered;

    /**
     * Информация о текущем пользователе и его выборе.
     */
    private QueueEntryData queueEntryData;

    public SpecializationUiController(QueueService queueService,
                                      SpecializationService specializationService,
                                      CodeService codeService) {
        this.queueService = queueService;
        this.specializationService = specializationService;
        this.codeService = codeService;
    }

    /**
     * Метод выбора специализации.
     *
     * @param specialization Выбранная специализация.
     * @return Представление специализаций.
     */
    public String chooseSpecialization(SpecializationMapper specialization) {
        this.specializationChosen = true;
        this.queueEntryData = new QueueEntryData(specialization, UUID.randomUUID());
        this.queueEntryData.setUserQueueCode(
                this.codeService.createUserQueueCode(
                        this.queueEntryData.getSpecialization().getName()));
        return "specializations.xhtml";
    }

    /**
     * Метод входа в очередь и запуска цикла проверки
     * на факт того, принят ли пользователь оператором.
     */
    public void enterQueue() {
        if (!this.queueRegistered) {
            this.queueService.createQueueEntry(this.queueEntryData);
            this.queueRegistered = true;
        }
        this.queueService.enterQueue(this.queueEntryData);
        this.checkAccept();
    }

    /**
     * Метод проверки (цикл) на факт того, принят ли пользователь оператором.
     */
    public void checkAccept() {
        while (true) {
            Optional<Operator> operatorOptional = this.queueService.checkAccept(this.queueEntryData);
            if (operatorOptional.isPresent()) {
                UUID userUUID = this.queueEntryData.getUserUUID();
                this.queueService.setOperator(userUUID, operatorOptional);
                this.queueService.setDateStart(userUUID, Timestamp.from(Instant.now()));
                super.redirect("serve");
                return;
            }
        }
    }

    /**
     * Метод сброса актуальной в рамках сессии информации.
     */
    public void resetSessionInfo() {
        this.specializationChosen = false;
        this.queueEntryData = null;
        this.queueRegistered = false;
    }

    /**
     * Метод получения списка специализаций.
     *
     * @return Список специализаций.
     */
    public List<SpecializationMapper> getSpecializations() {
        return this.specializationService.getSpecializations();
    }

    public String getActiveSpecializationName() {
        return this.queueEntryData.getSpecialization().getName();
    }

    public String getUserQueueCode() {
        return this.queueEntryData.getUserQueueCode();
    }

    public boolean isSpecializationChosen() {
        return this.specializationChosen;
    }
}
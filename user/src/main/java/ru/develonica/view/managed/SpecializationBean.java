package ru.develonica.view.managed;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.develonica.controller.ui.AbstractUiController;
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
@ManagedBean(name = "specializationBean")
@ViewScoped
@Component
public class SpecializationBean extends AbstractUiController {

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
     * Переключатель "принят ли пользователь (оператором)"
     */
    private boolean userAccepted;

    /**
     * Информация о текущем пользователе и его выборе.
     */
    private QueueEntryData queueEntryData;

    public SpecializationBean(QueueService queueService,
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
     */
    public void chooseSpecialization(SpecializationMapper specialization) {
        this.specializationChosen = true;
        this.queueEntryData = new QueueEntryData(specialization, UUID.randomUUID());
        this.queueEntryData.setUserQueueCode(
                this.codeService.createUserQueueCode(
                        this.queueEntryData.getSpecialization().getName()));
        this.enterQueue();
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
    }

    /**
     * Метод установки данных для обслуживания, если оператор назначен (пользователь принят этим оператором).
     */
    public void checkAccepted() {
        Optional<Operator> operatorOptional = this.queueService.checkAccept(this.queueEntryData);
        if (operatorOptional.isPresent()) {
            UUID userUUID = this.queueEntryData.getUserUUID();
            this.queueService.setOperator(userUUID, operatorOptional);
            this.queueService.setDateStart(userUUID, Timestamp.from(Instant.now()));
            this.userAccepted = true;
        } else {
            this.userAccepted = false;
        }
    }

    /**
     * Метод, возвращающий в качестве JSON формата ответ на вопрос - принят ли пользователь.
     *
     * @return JSON. accepted:true/false
     */
    public String getAcceptedAsJson() {
        this.checkAccepted();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accepted", this.userAccepted);
        return jsonObject.toString();
    }

    /**
     * Метод сброса актуальной информации managed bean'а.
     */
    public void resetBeanInfo() {
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
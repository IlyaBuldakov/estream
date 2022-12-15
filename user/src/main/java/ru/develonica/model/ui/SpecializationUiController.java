package ru.develonica.model.ui;

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
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.service.CodeService;
import ru.develonica.model.service.QueueService;
import ru.develonica.model.service.SpecializationService;

/**
 * UI-контроллер специализаций.
 * Контроллер представления специализаций.
 */
@ManagedBean(name = "specializationUiController")
@SessionScope
@Component
public class SpecializationUiController extends AbstractUiController {

    private final QueueService queueService;

    private final SpecializationService specializationService;

    private final CodeService codeService;

    private boolean specializationChosen;

    private boolean queueRegistered;

    private QueueEntryData queueEntryData;

    public SpecializationUiController(QueueService queueService,
                                      SpecializationService specializationService,
                                      CodeService codeService) {
        this.queueService = queueService;
        this.specializationService = specializationService;
        this.codeService = codeService;
    }

    public boolean isSpecializationChosen() {
        return specializationChosen;
    }

    public String chooseSpecialization(SpecializationMapper specialization) {
        this.specializationChosen = true;
        this.queueEntryData = new QueueEntryData(specialization, UUID.randomUUID());
        this.queueEntryData.setUserQueueCode(
                this.codeService.createUserQueueCode(
                        this.queueEntryData.getSpecialization().getName()));
        return "specializations.xhtml";
    }

    public List<SpecializationMapper> getSpecializations() {
        return this.specializationService.getSpecializations();
    }

    public void sendRequests() {
        if (!this.queueRegistered) {
            this.queueService.createQueueEntry(this.queueEntryData);
            this.queueRegistered = true;
        }
        this.queueService.sendRequests(this.queueEntryData);
        checkAccept();
    }

    public String getActiveSpecializationName() {
        return this.queueEntryData.getSpecialization().getName();
    }

    public String getUserQueueCode() {
        return this.queueEntryData.getUserQueueCode();
    }

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
     * Метод сброса информации о текущей выбранной специализации.
     */
    public void resetSessionInfo() {
        this.specializationChosen = false;
        this.queueEntryData = null;
        this.queueRegistered = false;
    }
}
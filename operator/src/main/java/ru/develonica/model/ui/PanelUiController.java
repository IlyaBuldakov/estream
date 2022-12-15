package ru.develonica.model.ui;

import java.util.List;
import java.util.Optional;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.develonica.model.Operator;
import ru.develonica.model.QueueEntryData;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.service.OperatorService;
import ru.develonica.model.service.SpecializationService;
import ru.develonica.security.OperatorSecurity;

@ManagedBean(name = "panelUiController")
@SessionScope
@Component
public class PanelUiController extends AbstractUiController {

    @PersistenceContext
    private final EntityManager entityManager;

    private final SpecializationService specializationService;

    private final OperatorService operatorService;

    private Operator currentOperator;

    private QueueEntryData queueEntryData;

    private boolean isThereUserToServe;

    private SpecializationMapper specializationFromRequest;

    public PanelUiController(EntityManager entityManager,
                             SpecializationService specializationService,
                             OperatorService operatorService) {
        this.entityManager = entityManager;
        this.operatorService = operatorService;
        this.specializationService = specializationService;
    }

    /**
     * Получение всех специализаций.
     *
     * @return Список специализаций.
     */
    @Transactional
    public List<SpecializationMapper> getCurrentOperatorSpecializations() {
        loadOperator();
        /*
        Получение свежего списка специализаций, независимо от
        того, каким образом они были добавлены в БД.
         */
        this.entityManager.refresh(currentOperator);
        return this.currentOperator.getSpecializations();
    }

    /**
     * Получение списка всех специализаций.
     *
     * @return Список всех специализаций.
     */
    public List<SpecializationMapper> getSpecializations() {
        return specializationService.getSpecializations();
    }

    /**
     * Метод загрузки оператора из {@link SecurityContext}.
     */
    public void loadOperator() {
        String currentOperatorEmail =
                ((OperatorSecurity) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal())
                        .getUsername();
        this.currentOperator = this.operatorService.getByEmail(currentOperatorEmail);
    }

    public String setOperatorActive(boolean value) {
        this.operatorService.setOperatorActive(this.currentOperator, value);
        this.loadOperator();
        return "panel.xhtml";
    }

    /**
     * Метод начала цикла очереди.
     */
    public void getRequest() {
        Optional<QueueEntryData> queueEntryDataOptional
                = this.operatorService.getRequest(this.currentOperator);
        if (queueEntryDataOptional.isPresent()) {
            QueueEntryData queueEntryData = queueEntryDataOptional.get();
            this.specializationFromRequest = queueEntryData.getSpecialization();
            thereIsUserToServe(true);
            this.queueEntryData = queueEntryData;
        }
    }

    public void acceptUser() {
        this.operatorService
                .acceptPair(this.queueEntryData);
        this.operatorService
                .setUserUuid(this.currentOperator, Optional.of(this.queueEntryData.getUserUUID()));
        super.redirect("serve");
    }

    /**
     * Метод завершения цикла работы.
     *
     * @return Редирект на представление с панелью.
     */
    public String stopWork() {
        this.thereIsUserToServe(false);
        this.setOperatorActive(false);
        this.queueEntryData = null;
        this.specializationFromRequest = null;
        this.operatorService.setUserUuid(this.currentOperator, Optional.empty());
        return "panel.xhtml?faces-redirect=true";
    }

    /**
     * Метод, возвращающий значение - есть ли
     * пользователь, которого нужно обслужить.
     *
     * @return Boolean - есть ли пользователь, которого нужно обслужить.
     */
    public boolean isThereUserToServe() {
        return isThereUserToServe;
    }

    /**
     * Метод - переключатель параметра
     * "есть ли пользователь, которого нужно обслужить."
     */
    public void thereIsUserToServe(boolean flag) {
        this.isThereUserToServe = flag;
    }

    public SpecializationMapper getSpecializationFromRequest() {
        return specializationFromRequest;
    }

    public boolean isOperatorActive() {
        loadOperator();
        return this.currentOperator.isActive();
    }

    public String getUserQueueCode() {
        return this.queueEntryData.getUserQueueCode();
    }

    public void setOperator(Operator currentOperator) {
        this.currentOperator = currentOperator;
    }
}
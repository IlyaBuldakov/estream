package ru.develonica.model.ui;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.develonica.model.mapper.OperatorMapper;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.service.OperatorService;
import ru.develonica.model.service.QueueHandler;
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

    private final QueueHandler queueHandler;

    private OperatorMapper currentOperator;

    private boolean isThereUserToServe;

    private SpecializationMapper specializationFromRequest;

    public PanelUiController(EntityManager entityManager,
                             SpecializationService specializationService,
                             OperatorService operatorService,
                             QueueHandler queueHandler) {
        this.entityManager = entityManager;
        this.operatorService = operatorService;
        this.specializationService = specializationService;
        this.queueHandler = queueHandler;
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

    public void setOperatorActive() {
        this.operatorService.setOperatorActive(this.currentOperator);
        super.redirect("panel");
    }

    /**
     * Метод начала цикла очереди.
     */
    @Async
    public void startQueueLoop() {
        boolean userWaitingOperator
                = this.queueHandler.startOperatorLoop(this.currentOperator);
        if (userWaitingOperator) {
            this.specializationFromRequest = this.queueHandler.getCurrentSpecialization();
            thereIsUserToServe(true);
        }
    }

    /**
     * Метод завершения цикла работы.
     *
     * @return Редирект на представление с панелью.
     */
    public String endQueueLoop() {
        thereIsUserToServe(false);
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
}
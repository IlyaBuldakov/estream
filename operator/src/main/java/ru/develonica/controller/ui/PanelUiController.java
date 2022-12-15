package ru.develonica.controller.ui;

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
import ru.develonica.model.service.QueuePotentialPairHolder;
import ru.develonica.model.service.SpecializationService;
import ru.develonica.security.OperatorDetails;

/**
 * UI контроллер для обработки ввода на странице панели.
 */
@ManagedBean(name = "panelUiController")
@SessionScope
@Component
public class PanelUiController extends AbstractUiController {

    @PersistenceContext
    private final EntityManager entityManager;

    private final SpecializationService specializationService;

    private final OperatorService operatorService;

    /**
     * Текущий оператор.
     */
    private Operator currentOperator;

    /**
     * Класс, хранящий информацию о текущем пользователе и его выборе.
     */
    private QueueEntryData queueEntryData;

    /**
     * Переключатель "есть ли в данный момент пользователь,
     * которого нужно обслужить".
     */
    private boolean isThereUserToServe;

    /**
     * Специализация из запроса (запроса пользователя).
     */
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
        this.loadOperator();
        /*
        Получение свежего списка специализаций, независимо от
        того, каким образом они были добавлены в БД.
         */
        this.entityManager.refresh(this.currentOperator);
        return this.currentOperator.getSpecializations();
    }

    /**
     * Получение списка всех специализаций.
     *
     * @return Список всех специализаций.
     */
    public List<SpecializationMapper> getSpecializations() {
        return this.specializationService.getSpecializations();
    }

    /**
     * Метод загрузки оператора из {@link SecurityContext}.
     */
    private void loadOperator() {
        String currentOperatorEmail =
                ((OperatorDetails) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal())
                        .getUsername();
        this.currentOperator = this.operatorService.getByEmail(currentOperatorEmail);
    }

    /**
     * Устанавливает переключатель "активен ли оператор" для текущего оператора,
     * освежает оператора (currentOperator) при помощи {@link #loadOperator()}.
     *
     * @param value Значение true/false.
     * @return Представление с панелью.
     */
    public String setOperatorActive(boolean value) {
        this.operatorService.setOperatorActive(this.currentOperator, value);
        this.loadOperator();
        return "panel.xhtml";
    }

    /**
     * Метод получения запроса от пользователя, которому подходит текущий оператор.
     * Проверяет, есть ли в словаре {@link QueuePotentialPairHolder} запись от пользователя,
     * соответствующая текущему оператору.
     */
    public void getRequestFromQueue() {
        Optional<QueueEntryData> queueEntryDataOptional
                = this.operatorService.getRequestFromQueue(this.currentOperator);
        if (queueEntryDataOptional.isPresent()) {
            QueueEntryData queueEntryData = queueEntryDataOptional.get();
            this.specializationFromRequest = queueEntryData.getSpecialization();
            thereIsUserToServe(true);
            this.queueEntryData = queueEntryData;
        }
    }

    /**
     * Метод принятия пользователя оператором.
     */
    public void acceptUser() {
        if (this.operatorService
                .acceptPair(this.queueEntryData, this.currentOperator)) {
            this.operatorService
                    .setUserUuid(this.currentOperator, Optional.of(this.queueEntryData.getUserUUID()));
            super.redirect("serve");
            return;
        }
        super.redirect("panel");
    }

    /**
     * Метод завершения работы.
     * 1. Установка параметра "имеется пользователь, которого нужно обслужить", значение false.
     * 2. Установка параметра "активен ли оператор", значение false.
     * 3. Обнуление переменной queueEntryData - локальная запись о пользователе,
     * которого есть возможность обслужить
     * 4. Обнуление переменной specializationFromRequest - локальная запись о специализации,
     * которая получается из запроса пользователя.
     * 5. Обнуление поля "пользователь", у оператора (которого он обслуживает в данный момент).
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
        return this.isThereUserToServe;
    }

    /**
     * Метод - переключатель параметра
     * "есть ли пользователь, которого нужно обслужить."
     */
    public void thereIsUserToServe(boolean flag) {
        this.isThereUserToServe = flag;
    }

    public SpecializationMapper getSpecializationFromRequest() {
        return this.specializationFromRequest;
    }

    /**
     * Проверка - активен ли оператор в данный момент.
     * Для этого поле currentOperator освежается методом {@link #loadOperator()}
     *
     * @return Активен ли оператор.
     */
    public boolean isOperatorActive() {
        this.loadOperator();
        return this.currentOperator.isActive();
    }

    /**
     * Метод получения кода пользователя из поля queueEntryData - записи
     * о пользователе, которого есть возможность обслужить.
     *
     * @return Код пользователя в очереди.
     */
    public String getUserQueueCode() {
        return this.queueEntryData.getUserQueueCode();
    }

    /**
     * Установка текущего оператора для использования представлением
     * (из {@link SecurityContext}).
     *
     * @param currentOperator Текущий оператор.
     */
    public void setOperator(Operator currentOperator) {
        this.currentOperator = currentOperator;
    }

    /**
     * Метод сброса актуальной в рамках сессии информации.
     */
    public void resetSessionInfo() {
        this.isThereUserToServe = false;
        this.specializationFromRequest = null;
        this.queueEntryData = null;
    }
}
package ru.develonica.model.ui;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.develonica.model.mapper.OperatorMapper;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.OperatorRepository;
import ru.develonica.model.service.SpecializationService;
import ru.develonica.security.OperatorSecurity;

@ManagedBean(name = "panelUiController")
@SessionScoped
@Component
public class PanelUiController {

    @PersistenceContext
    private final EntityManager entityManager;

    private final OperatorRepository operatorRepository;

    private final SpecializationService specializationService;

    private OperatorMapper currentOperator;

    public PanelUiController(EntityManager entityManager,
                             OperatorRepository operatorRepository,
                             SpecializationService specializationService) {
        this.entityManager = entityManager;
        this.operatorRepository = operatorRepository;
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
        entityManager.refresh(currentOperator);
        return this.currentOperator.getSpecializations();
    }

    public List<SpecializationMapper> getSpecializations() {
        return specializationService.getSpecializations();
    }

    public void loadOperator() {
        String currentOperatorEmail =
                ((OperatorSecurity) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal())
                        .getUsername();
        this.currentOperator = operatorRepository.getByEmail(currentOperatorEmail);
    }
}
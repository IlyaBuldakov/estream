package ru.develonica.model.ui;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.springframework.stereotype.Component;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.service.SpecializationService;

@ManagedBean(name = "specializationUiController")
@SessionScoped
@Component
public class SpecializationUiController implements Serializable {

    private final SpecializationService specializationService;

    private boolean specializationChosen;

    private String activeSpecializationName;

    private String userQueueCode;

    public SpecializationUiController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    public List<SpecializationMapper> getSpecializations() {
        return specializationService.getSpecializations();
    }

    public String chooseSpecialization(SpecializationMapper specialization) {
        this.specializationChosen = true;
        this.activeSpecializationName = specialization.toString();
        this.userQueueCode = this.specializationService
                .chooseSpecialization(this.activeSpecializationName);
        return "specializations.xhtml";
    }

    public String cancelChoose() {
        this.specializationChosen = false;
        this.activeSpecializationName = null;
        return "specializations.xhtml";
    }

    public boolean isSpecializationChosen() {
        return specializationChosen;
    }

    public String getActiveSpecializationName() {
        return activeSpecializationName;
    }

    public String getUserQueueCode() {
        return userQueueCode;
    }
}
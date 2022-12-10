package ru.develonica.model.ui;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.springframework.stereotype.Component;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.SpecializationRepository;

@ManagedBean(name = "specializationUiController")
@SessionScoped
@Component
public class SpecializationUiController implements Serializable {

    private List<SpecializationMapper> specializationsList;

    private final SpecializationRepository specializationRepository;

    private boolean specializationChosen;

    private String activeSpecializationName;

    public SpecializationUiController(List<SpecializationMapper> specializationsList,
                                      SpecializationRepository specializationRepository) {
        this.specializationsList = specializationsList;
        this.specializationRepository = specializationRepository;
    }

    public List<SpecializationMapper> getSpecializationsList() {
        this.specializationsList = this.specializationRepository.findAll();
        return this.specializationsList;
    }

    public String chooseSpecialization(SpecializationMapper specialization) {
        this.specializationChosen = true;
        this.activeSpecializationName = specialization.toString();
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
}
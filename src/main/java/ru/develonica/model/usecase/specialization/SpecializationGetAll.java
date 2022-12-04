package ru.develonica.model.usecase.specialization;

import java.util.List;
import javax.faces.bean.ManagedBean;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.repository.SpecializationRepository;

@ManagedBean
@AllArgsConstructor
@Component
public class SpecializationGetAll {

    private SpecializationRepository specializationRepository;

    public List<SpecializationMapper> execute() {
        return specializationRepository.findAll();
    }
}

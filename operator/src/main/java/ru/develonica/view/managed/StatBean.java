package ru.develonica.view.managed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.develonica.controller.ui.AbstractUiController;
import ru.develonica.model.Operator;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.service.OperatorService;

@ManagedBean(name = "statBean")
@SessionScope
@Component
public class StatBean extends AbstractUiController {

    private final OperatorService operatorService;

    private final List<String> bgColors = new ArrayList<>();

    public StatBean(OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    public PieChartModel createPieModel() {
        PieChartModel pieModel = new PieChartModel();
        ChartData data = new ChartData();

        Operator currentOperator =
                (Operator) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<SpecializationMapper> specializations = currentOperator.getSpecializations();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();

        HashMap<String, Integer> specializationStatsByOperatorId
                = this.operatorService.getSpecializationStatsByOperator(currentOperator);

        for (Map.Entry<String, Integer> entry : specializationStatsByOperatorId.entrySet()) {
            values.add(entry.getValue());
            if (this.bgColors.size() < specializationStatsByOperatorId.size()) {
                this.bgColors.add(generateRandomRgb(entry.getKey()));
            }
        }

        data.setLabels(specializations.stream().map(SpecializationMapper::getName).toList());
        dataSet.setData(values);
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);

        pieModel.setData(data);

        return pieModel;
    }

    public String generateRandomRgb(String specializationName) {
        int randomColor = Math.abs(specializationName.hashCode() % 256);
        return "rgb(%s, %s, %s)"
                .formatted(
                        randomColor,
                        Math.abs(randomColor / 3 * (int) (Math.random() * 10)),
                        Math.abs(randomColor / 7 * (int) (Math.random() * 10))
                );
    }
}
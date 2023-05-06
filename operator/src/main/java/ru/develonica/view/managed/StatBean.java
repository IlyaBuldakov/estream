package ru.develonica.view.managed;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
import ru.develonica.model.Operator;
import ru.develonica.model.managed.BaseManagedBean;
import ru.develonica.model.mapper.ActivityMapper;
import ru.develonica.model.mapper.SpecializationMapper;
import ru.develonica.model.service.ActivityService;
import ru.develonica.model.service.OperatorService;
import ru.develonica.security.OperatorDetails;

@ManagedBean(name = "statBean")
@SessionScope
@Component
public class StatBean extends BaseManagedBean {

    private final OperatorService operatorService;

    private final ActivityService activityService;

    private final List<String> bgColors = new ArrayList<>();

    public StatBean(OperatorService operatorService,
                    ActivityService activityService) {
        this.operatorService = operatorService;
        this.activityService = activityService;
    }

    public boolean isWorkSessionEnabled() {
        OperatorDetails operator =
                (OperatorDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.operatorService.getByEmail(operator.getUsername()).isActive();
    }

    public long getWorkSessionDurationInMinutes() {
        ActivityMapper notFinishedActivitySession =
                this.activityService.findByActivityFinishIsNull();
        LocalDateTime activityStart = notFinishedActivitySession.getActivityStart();
        return (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                - activityStart.toEpochSecond(ZoneOffset.UTC)) / 60;

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
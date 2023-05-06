package ru.develonica.model.mapper;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "activity")
public class ActivityMapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "activity_start")
    private LocalDateTime activityStart;

    @Column(name = "activity_finish")
    private LocalDateTime activityFinish;

    public ActivityMapper(Long operatorId, LocalDateTime activityStart) {
        this.operatorId = operatorId;
        this.activityStart = activityStart;
    }

    public ActivityMapper() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setActivityFinish(LocalDateTime activityFinish) {
        this.activityFinish = activityFinish;
    }

    public LocalDateTime getActivityStart() {
        return activityStart;
    }
}

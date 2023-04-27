package ru.develonica.model.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Модель записи в очереди.
 */
@Entity
@Table(name = "queue")
public class QueueMapper {

    /**
     * Идентификатор.
     */
    @Id
    @Column(name = "queue_id", nullable = false)
    private UUID id;

    /**
     * Идентификатор оператора.
     */
    @Column(name = "operator_id")
    private Long operatorId;

    /**
     * Код пользователя в очереди.
     */
    @Column(name = "user_code")
    private String userQueueCode;

    /**
     * Дата создания (пользователь встал в очередь).
     */
    @Column(name = "date_create")
    private Timestamp dateCreate;

    /**
     * Дата начала обслуживания.
     */
    @Column(name = "date_start")
    private Timestamp dateStart;

    /**
     * Дата окончания обслуживания.
     */
    @Column(name = "date_finish")
    private Timestamp dateFinish;

    @Column(name = "specialization_id")
    private long specializationId;

    public QueueMapper() {
        this.dateCreate = Timestamp.from(Instant.now());
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public void setDateStart(Timestamp dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateFinish(Timestamp dateFinish) {
        this.dateFinish = dateFinish;
    }

    public void setUserQueueCode(String userQueueCode) {
        this.userQueueCode = userQueueCode;
    }

    public long getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(long specializationId) {
        this.specializationId = specializationId;
    }
}

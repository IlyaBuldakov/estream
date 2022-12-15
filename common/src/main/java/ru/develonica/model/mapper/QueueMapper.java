package ru.develonica.model.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Модель очереди.
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

    public QueueMapper() {
    }

    public QueueMapper(String userQueueCode) {
        this.dateCreate = Timestamp.from(Instant.now());
        this.userQueueCode = userQueueCode;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserQueueCode() {
        return userQueueCode;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public void setDateStart(Timestamp dateStart) {
        this.dateStart = dateStart;
    }

    public Timestamp getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Timestamp dateFinish) {
        this.dateFinish = dateFinish;
    }
}
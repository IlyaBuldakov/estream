package ru.develonica.model.mapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
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

    public QueueMapper() {}

    public QueueMapper(String userQueueCode) {
        this.dateCreate = Timestamp.from(Instant.now());
        this.userQueueCode = userQueueCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserQueueCode() {
        return userQueueCode;
    }
}

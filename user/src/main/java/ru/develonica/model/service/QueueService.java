package ru.develonica.model.service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import ru.develonica.model.Operator;
import ru.develonica.model.QueueEntryData;
import ru.develonica.model.mapper.QueueMapper;
import ru.develonica.model.repository.QueueRepository;

/**
 * Сервис для работы с очередями.
 */
@Service
public class QueueService {

    private final QueueRepository queueRepository;

    private final QueuePotentialPairHolder queuePotentialPairHolder;

    public QueueService(QueueRepository queueRepository,
                        QueuePotentialPairHolder queuePotentialPairHolder) {
        this.queueRepository = queueRepository;
        this.queuePotentialPairHolder = queuePotentialPairHolder;
    }

    /**
     * Метод входа в очередь (добавление информации о текущем
     * пользователе и его выборе в очередь).
     */
    public void enterQueue(QueueEntryData queueEntryData) {
        this.queuePotentialPairHolder.putPair(queueEntryData);
    }

    /**
     * Метод установки оператора. Устанавливает оператора
     * для записи очереди. Если оператор не предоставлен ({@link Optional#empty()}),
     * устанавливается значение null.
     *
     * @param currentUserId UUID пользователя (идентификатор очереди).
     * @param operator      Оператор.
     */
    public void setOperator(UUID currentUserId, Optional<Operator> operator) {
        Optional<QueueMapper> queueMapperOptional
                = this.queueRepository.findById(currentUserId);
        if (queueMapperOptional.isPresent()) {
            QueueMapper queueMapper = queueMapperOptional.get();
            if (operator.isPresent()) {
                queueMapper.setOperatorId(operator.get().getId());
                this.queueRepository.save(queueMapper);
            } else {
                queueMapper.setOperatorId(null);
                this.queueRepository.save(queueMapper);
            }
        }
    }

    /**
     * Метод установки даты начала обслуживания.
     *
     * @param currentUserUuid UUID пользователя (идентификатор очереди).
     * @param dateStart       Дата начала обслуживания.
     */
    public void setDateStart(UUID currentUserUuid, Timestamp dateStart) {
        Optional<QueueMapper> queueMapperOptional
                = this.queueRepository.findById(currentUserUuid);
        if (queueMapperOptional.isPresent()) {
            QueueMapper queueMapper = queueMapperOptional.get();
            queueMapper.setDateStart(dateStart);
            this.queueRepository.save(queueMapper);
        }
    }

    /**
     * Метод установки даты конца обслуживания.
     *
     * @param currentUserUuid UUID пользователя (идентификатор очереди).
     * @param dateFinish Дата конца обслуживания.
     */
    public void setDateFinish(UUID currentUserUuid, Timestamp dateFinish) {
        Optional<QueueMapper> queueMapperOptional
                = this.queueRepository.findById(currentUserUuid);
        if (queueMapperOptional.isPresent()) {
            QueueMapper queueMapper = queueMapperOptional.get();
            queueMapper.setDateFinish(dateFinish);
            this.queueRepository.save(queueMapper);
        }
    }

    /**
     * Метод создания элемента очереди (записи в таблице).
     */
    public void createQueueEntry(QueueEntryData queueEntryData) {
        QueueMapper queueMapper = new QueueMapper();
        queueMapper.setId(queueEntryData.getUserUUID());
        queueMapper.setUserQueueCode(queueEntryData.getUserQueueCode());
        this.queueRepository.save(queueMapper);
    }

    /**
     * Метод проверки - принят ли пользователь.
     *
     * @param queueEntryData Информация о текущем пользователе и его выборе.
     * @return Оператор в {@link Optional}.
     */
    public Optional<Operator> checkAccept(QueueEntryData queueEntryData) {
        return this.queuePotentialPairHolder.checkAccept(queueEntryData);
    }
}
package ru.develonica.model.service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import ru.develonica.model.Operator;
import ru.develonica.model.QueueEntryData;
import ru.develonica.model.mapper.CodeMapper;
import ru.develonica.model.mapper.QueueMapper;
import ru.develonica.model.repository.CodeRepository;
import ru.develonica.model.repository.QueueRepository;

/**
 * Сервис для работы с очередями.
 */
@Service
public class QueueService {

    private final QueueRepository queueRepository;

    private final CodeRepository codeRepository;

    private final QueuePotentialPairHolder queuePotentialPairHolder;

    private final CodeResolver codeResolver;

    public QueueService(QueueRepository queueRepository,
                        CodeRepository codeRepository,
                        QueuePotentialPairHolder queuePotentialPairHolder,
                        CodeResolver codeResolver) {
        this.queueRepository = queueRepository;
        this.codeRepository = codeRepository;
        this.queuePotentialPairHolder = queuePotentialPairHolder;
        this.codeResolver = codeResolver;
    }

    /**
     * Отправка запросов подходящим операторам. Ищет всех активных операторов,
     * которые обладают нужной специализацией и кладет запись со
     * {@link QueueEntryData своим UUID и специализацией} в словарь {@link QueuePotentialPairHolder}.
     */
    public void sendRequests(QueueEntryData queueEntryData) {
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

    public void setDateFinish(UUID currentUserUuid, Timestamp dateStart) {
        Optional<QueueMapper> queueMapperOptional
                = this.queueRepository.findById(currentUserUuid);
        if (queueMapperOptional.isPresent()) {
            QueueMapper queueMapper = queueMapperOptional.get();
            queueMapper.setDateFinish(dateStart);
            this.queueRepository.save(queueMapper);
        }
    }

    /**
     * Метод создания элемента очереди (записи в таблице).
     *
     * @return {@link QueueMapper}.
     */
    public QueueMapper createQueueEntry(QueueEntryData queueEntryData) {
        CodeMapper codeMapper = this.codeRepository.findBySequenceName(queueEntryData.getSpecialization().getName());
        QueueMapper queueMapper = new QueueMapper(this.codeResolver.resolve(codeMapper));
        queueMapper.setId(queueEntryData.getUserUUID());
        this.queueRepository.save(queueMapper);
        return queueMapper;
    }

    public Optional<Operator> checkAccept(QueueEntryData queueEntryData) {
        return this.queuePotentialPairHolder.checkAccept(queueEntryData);
    }
}
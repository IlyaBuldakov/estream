package ru.develonica.model.service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import ru.develonica.model.Operator;
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

    private final CodeResolver codeResolver;

    public QueueService(QueueRepository queueRepository,
                        CodeRepository codeRepository, CodeResolver codeResolver) {
        this.queueRepository = queueRepository;
        this.codeRepository = codeRepository;
        this.codeResolver = codeResolver;
    }

    /**
     * Метод установки оператора.
     *
     * @param currentUserId UUID пользователя (идентификатор очереди).
     * @param operator Оператор.
     */
    public void setOperator(UUID currentUserId, Operator operator) {
        Optional<QueueMapper> queueMapperOptional
                = this.queueRepository.findById(currentUserId);
        if (queueMapperOptional.isPresent()) {
            QueueMapper queueMapper = queueMapperOptional.get();
            queueMapper.setOperatorId(operator.getId());
            this.queueRepository.save(queueMapper);
        }
    }

    /**
     * Метод установки даты начала обслуживания.
     *
     * @param currentUserUuid UUID пользователя (идентификатор очереди).
     * @param dateStart Дата начала обслуживания.
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
     * Метод создания элемента очереди (записи в таблице).
     *
     * @param specializationName Имя специализации.
     * @return {@link QueueMapper}.
     */
    public QueueMapper createQueueEntry(String specializationName) {
        CodeMapper codeMapper = this.codeRepository.findBySequenceName(specializationName);
        QueueMapper queueMapper = new QueueMapper(this.codeResolver.resolve(codeMapper));
        this.queueRepository.save(queueMapper);
        return queueMapper;
    }
}
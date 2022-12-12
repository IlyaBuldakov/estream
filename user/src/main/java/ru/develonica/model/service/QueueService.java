package ru.develonica.model.service;

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

    public void setOperator(UUID currentUserId, Operator operator) {
        Optional<QueueMapper> queueMapperOptional = queueRepository.findById(currentUserId);
        if (queueMapperOptional.isPresent()) {
            QueueMapper queueMapper = queueMapperOptional.get();
            queueMapper.setOperatorId(operator.getId());
            queueRepository.save(queueMapper);
        }
    }

    /**
     * Метод создания элемента очереди (записи в таблице).
     *
     * @param specializationName Имя специализации.
     * @return {@link QueueMapper}.
     */
    public QueueMapper createQueueEntry(String specializationName) {
        CodeMapper codeMapper = codeRepository.findBySequenceName(specializationName);
        QueueMapper queueMapper = new QueueMapper(codeResolver.resolve(codeMapper));
        queueRepository.save(queueMapper);
        return queueMapper;
    }
}
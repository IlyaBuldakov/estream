package ru.develonica.model.service;

import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.CodeMapper;
import ru.develonica.model.mapper.QueueMapper;
import ru.develonica.model.repository.CodeRepository;
import ru.develonica.model.repository.QueueRepository;

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

    public QueueMapper createQueueEntry(String specializationName) {
        CodeMapper codeMapper = codeRepository.findBySequenceName(specializationName);
        QueueMapper queueMapper = new QueueMapper(codeResolver.resolve(codeMapper));
        queueRepository.save(queueMapper);
        return queueMapper;
    }
}
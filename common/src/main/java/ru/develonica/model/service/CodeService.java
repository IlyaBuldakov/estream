package ru.develonica.model.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.CodeMapper;
import ru.develonica.model.repository.CodeRepository;

/**
 * Сервис для работы с кодом пользователя в очереди.
 */
@Service
public class CodeService {

    private static final Logger LOG = LoggerFactory.getLogger(CodeService.class);

    /**
     * Алфавит из которого берутся буквы кода.
     */
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * SQL-запрос для выполнения процедуры создания последовательности.
     */
    private static final String CALL_SEQ_GENERATION_PROCEDURE_QUERY = "call generate_sequence(?)";

    private static final String CALL_SEQ_DROP_PROCEDURE_QUERY = "call drop_sequence(?)";

    @PersistenceContext
    private final EntityManager entityManager;

    private final CodeRepository codeRepository;

    private final CodeResolver codeResolver;

    public CodeService(EntityManager entityManager,
                       CodeRepository codeRepository,
                       CodeResolver codeResolver) {
        this.entityManager = entityManager;
        this.codeRepository = codeRepository;
        this.codeResolver = codeResolver;
    }

    /**
     * Метод создания кода пользователя в очереди по имени специализации.
     *
     * @param specializationName Имя специализации.
     * @return Код пользователя в очереди.
     */
    public String createUserQueueCode(String specializationName) {
        CodeMapper codeMapper = this.codeRepository.findBySequenceName(specializationName);
        return this.codeResolver.resolve(codeMapper);
    }

    /**
     * Этот метод создает одноименную последовательность для специализации,
     * а также создает запись в таблице кодов со случайной буквой
     * и именем последовательности. Примеры записей, которые этот метод создает в таблице:
     * 1 | Расчёт ОСАГО | B
     * 2 | Постановка автомобиля на учёт | K
     * 3 | Получение В/У | L
     *
     * @param specializationName Имя специализации.
     */
    @Transactional
    public void generateCodeEntry(String specializationName) {
        generateSequence(specializationName);
        createCodeMapping(specializationName);
    }

    @Transactional
    public void deleteCodeEntry(String specializationName) {
        dropSequence(specializationName);
        deleteCodeMapping(specializationName);
    }

    /**
     * Метод создания одноименной последовательности для специализации.
     *
     * @param specializationName Имя специализации.
     */
    private void generateSequence(String specializationName) {
        Query query = this.entityManager.createNativeQuery(CALL_SEQ_GENERATION_PROCEDURE_QUERY);
        query.setParameter(1, specializationName);
        query.executeUpdate();
        LOG.info("Создана последовательность %s".formatted(specializationName));
    }

    public void dropSequence(String specializationName) {
        Query query = this.entityManager.createNativeQuery(CALL_SEQ_DROP_PROCEDURE_QUERY);
        query.setParameter(1, specializationName);
        query.executeUpdate();
        LOG.info("Удалена последовательность %s".formatted(specializationName));
    }

    /**
     * Метод создания записи со случайной буквой и именем последовательности.
     * (из которой будет браться числовое значение для генерации кода очереди
     * для конкретной специализации)
     *
     * @param specializationName Имя специализации.
     */
    private void createCodeMapping(String specializationName) {
        double randomDouble = Math.random() / Math.nextDown(1.0);
        int randomIntResult = (int) ((1.0 - randomDouble) + ALPHABET.length * randomDouble);
        CodeMapper codeMapper = new CodeMapper(ALPHABET[randomIntResult], specializationName);
        this.codeRepository.save(codeMapper);
        LOG.info("Специализации \"%s\" выдана буква %s"
                .formatted(specializationName, codeMapper.getCodeLetter()));
    }

    private void deleteCodeMapping(String specializationName) {
        CodeMapper codeMapper = this.codeRepository.findBySequenceName(specializationName);
        this.codeRepository.delete(codeMapper);
        LOG.info("Запись о коде %s с последовательностью %s была удалена"
                .formatted(codeMapper.getCodeLetter(), codeMapper.getSequenceName()));
    }
}
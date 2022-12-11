package ru.develonica.model.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.CodeMapper;
import ru.develonica.model.repository.CodeRepository;

/**
 * Сервис для работы с кодом пользователя в очереди.
 */
@Service
public class CodeService {

    /**
     * Алфавит из которого берутся буквы кода.
     */
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * SQL-запрос для выполнения процедуры создания последовательности.
     */
    private static final String CALL_SEQ_GENERATION_PROCEDURE_QUERY = "call generate_sequence(?)";

    @PersistenceContext
    private final EntityManager entityManager;

    private final CodeRepository codeRepository;

    public CodeService(EntityManager entityManager, CodeRepository codeRepository) {
        this.entityManager = entityManager;
        this.codeRepository = codeRepository;
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

    /**
     * Метод создания одноименной последовательности для специализации.
     *
     * @param specializationName Имя специализации.
     */
    private void generateSequence(String specializationName) {
        Query query = entityManager.createNativeQuery(CALL_SEQ_GENERATION_PROCEDURE_QUERY);
        query.setParameter(1, specializationName);
        query.executeUpdate();
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
        codeRepository.save(new CodeMapper(ALPHABET[randomIntResult], specializationName));
    }
}
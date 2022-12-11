package ru.develonica.model.service;

import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.CodeMapper;
import ru.develonica.model.repository.CodeRepository;

@Service
public class CodeService {

    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static final String CALL_SEQ_GENERATION_PROCEDURE_QUERY = "call generate_sequence(?)";

    @PersistenceContext
    private final EntityManager entityManager;

    private final CodeRepository codeRepository;

    public CodeService(EntityManager entityManager, CodeRepository codeRepository) {
        this.entityManager = entityManager;
        this.codeRepository = codeRepository;
    }

    @Transactional
    public void generateCode(String specializationName) throws SQLException {
        Query query = entityManager.createNativeQuery(CALL_SEQ_GENERATION_PROCEDURE_QUERY);
        query.setParameter(1, specializationName);
        query.executeUpdate();
        createCodeMapping(specializationName);
    }

    private void createCodeMapping(String specializationName) {
        double randomDouble = Math.random() / Math.nextDown(1.0);
        int randomIntResult = (int) ((1.0 - randomDouble) + ALPHABET.length * randomDouble);
        codeRepository.save(new CodeMapper(ALPHABET[randomIntResult], specializationName));
    }
}
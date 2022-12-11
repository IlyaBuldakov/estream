package ru.develonica.model.service;

import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Component;
import ru.develonica.model.mapper.CodeMapper;

@Component
public class CodeResolver {

    private static final String LAST_SEQ_ELEM_QUERY = "SELECT nextval('\"%s\"')";

    @PersistenceContext
    private final EntityManager entityManager;

    public CodeResolver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public String resolve(CodeMapper codeMapper) {
        StringBuilder sb = new StringBuilder();
        String sql = String.format(LAST_SEQ_ELEM_QUERY, codeMapper.getSequenceName());
        Query query = entityManager.createNativeQuery(sql);
        BigInteger result = (BigInteger) query.getSingleResult();

        sb.append(Character.toUpperCase(codeMapper.getCodeLetter()));
        sb.append(result);
        return sb.toString();
    }
}

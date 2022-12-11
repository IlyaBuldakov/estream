package ru.develonica.model.service;

import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Component;
import ru.develonica.model.mapper.CodeMapper;

/**
 * Класс, отвечающий за генерацию кода очереди.
 * Сценарий работы:
 * 1. Получает на вход {@link CodeMapper}, из которого
 * достает букву кода и имя последовательности.
 * 2. Берет значение из последовательности,
 * имя которой он получил.
 * 3. Это значение и букву кода склеивает в строку.
 */
@Component
public class CodeResolver {

    private static final String LAST_SEQ_ELEM_QUERY = "SELECT nextval('\"%s\"')";

    @PersistenceContext
    private final EntityManager entityManager;

    public CodeResolver(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Основной метод преобразования кода очереди.
     *
     * @param codeMapper {@link CodeMapper}.
     * @return Строковое представление кода пользователя
     * в очереди. Пример: G102, P11, U84
     */
    public String resolve(CodeMapper codeMapper) {
        StringBuilder sb = new StringBuilder();
        String sql = String.format(LAST_SEQ_ELEM_QUERY, codeMapper.getSequenceName());
        Query query = entityManager.createNativeQuery(sql);
        BigInteger result = (BigInteger) query.getSingleResult();

        sb.append(codeMapper.getCodeLetter());
        sb.append(result);
        return sb.toString();
    }
}

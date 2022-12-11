package ru.develonica.model.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import ru.develonica.model.mapper.CodeMapper;
import ru.develonica.model.repository.CodeRepository;

@Service
public class CodeService {

    private final static char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private final DataSource dataSource;

    private final CodeRepository codeRepository;

    public CodeService(DataSource dataSource, CodeRepository codeRepository) {
        this.dataSource = dataSource;
        this.codeRepository = codeRepository;
    }

    public void generateCode(String specializationName) throws SQLException {
        Connection connection = dataSource.getConnection();
        String query = "call generate_sequence(?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, specializationName);
        preparedStatement.execute();

        createCodeMapping(specializationName);
    }

    private void createCodeMapping(String specializationName) {
        double randomDouble = Math.random() / Math.nextDown(1.0);
        int randomIntResult = (int) ((1.0 - randomDouble) + ALPHABET.length * randomDouble);
        codeRepository.save(new CodeMapper(ALPHABET[randomIntResult], specializationName));
    }
}
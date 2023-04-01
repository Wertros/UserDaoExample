package com.adam.dao;


import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoTest {

    UserDao userDao = new UserDao(Environment.TEST);

    @BeforeEach
    void setup() {
        cleanup();
    }

    @Test
    void createShouldSaveUserInDatabase() {
        //given
        User john = User.john();

        //when
        User persistedJohn = userDao.create(john);

        //then
        assertThat(persistedJohn)
                .usingRecursiveComparison(ignoreIdField())
                .isEqualTo(john);

        assertThat(persistedJohn.getId()).isGreaterThan(0);
    }

    @Test
    void createShouldSavePasswordAsHash() {
        //given
        User john = User.john();
        String rawPassword = john.getPassword();

        //when
        User persistedJohn = userDao.create(john);
        String hashedPass = fetchPass(persistedJohn.getId()).get();
        
        //then
        assertThat(rawPassword).isNotEqualTo(hashedPass);
        assertThat(BCrypt.checkpw(rawPassword, hashedPass)).isTrue();
    }

    private static RecursiveComparisonConfiguration ignoreIdField() {
        return RecursiveComparisonConfiguration.builder()
                .withIgnoredFields("id")
                .build();
    }

    /**
     * Method used for testing
     */
    public static void cleanup() {
        try (Connection conn = DbUtil.connect(Environment.TEST);
             PreparedStatement statement = conn.prepareStatement("DELETE FROM users WHERE 1=1")) {

            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Optional<String> fetchPass(long userId) {
        try (Connection conn = DbUtil.connect(Environment.TEST);
             PreparedStatement statement = conn.prepareStatement("SELECT password FROM users WHERE id = ?")) {
 
            statement.setLong(1, userId);
            try(ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(resultSet.getString("password")) : Optional.empty(); 
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch password for id: " + userId, e);
        }
    }


}
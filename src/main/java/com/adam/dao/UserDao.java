package com.adam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDao {

    private static final String INSERT_USER_QUERY =
            "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    
    private final Environment environment;
    private final PasswordEncoder passwordEncoder;

    public UserDao(Environment environment) {
        this.environment = environment;
        this.passwordEncoder = new PasswordEncoder();
    }

    public User create(User user) {
        try (Connection connect = DbUtil.connect(environment);
             PreparedStatement statement = connect.prepareStatement(INSERT_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getEmail());
            statement.setString(3, passwordEncoder.encode(user.getPassword()));
            statement.executeUpdate();

            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    return user.withId(id);
                } else {
                    throw new RuntimeException("Failed get generated ID");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> read(long id) {
        try (Connection connect = DbUtil.connect(environment);
             PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM USERS WHERE id = ?")
        ) {

            try(ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    User user = User.fromResultSet(resultSet);
                    return Optional.of(user);    
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(User user) {

    } 

    public void delete(long id) {

    }

}

package com.adam.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private static final int DEFAULT_ID = 0;
    private static final int ID_COLUMN_POSITION = 1;

    private final long id;
    private final String firstName;
    private final String email;
    private final String password;

    /**
     *  Creates {@code User} instance object from {@code ResultSet} class
    * */
    public static User fromResultSet(ResultSet resultSet) throws SQLException {
        long generatedId = resultSet.getLong(ID_COLUMN_POSITION);
        String fistName = resultSet.getString("username");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        
        return new User(generatedId, fistName, email, password);
    }
    
    public static User john() {
        return new User(DEFAULT_ID, "john", "john@gmail.com", "johnpassword");
    }
    
    public static User alice() {
        return new User(DEFAULT_ID, "alice", "alice@gmail.com", "alicepassword");
    }
    
    private User(long id, String firstName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User withId(long id) {
        return new User(id, this.firstName, this.email, this.password);
    }
}

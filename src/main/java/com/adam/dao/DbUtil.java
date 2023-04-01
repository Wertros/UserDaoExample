package com.adam.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbUtil {

    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_URL_TEST = "jdbc:mysql://localhost:3307";
    
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "coderslab";

    private static final String USER_DAO_DATABASE = "userdao";

    public static Connection connect(Environment environment) throws SQLException {
        
        String url = getDatabase(environment) + "/" + USER_DAO_DATABASE; 
        return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
    }

    private static String getDatabase(Environment environment) {
        return Environment.PRODUCTION.equals(environment) ? DB_URL : DB_URL_TEST;
    }

    public static int insert(Connection conn, String query, String... params) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printData(Connection conn, String query, String... columnNames) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                for (String columnName : columnNames) {
                    System.out.println(resultSet.getString(columnName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String DELETE_QUERY = "DELETE FROM tableName where id = ?";

    public static void remove(Connection conn, String tableName, int id) {
        try (PreparedStatement statement = conn.prepareStatement(DELETE_QUERY.replace("tableName", tableName))) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

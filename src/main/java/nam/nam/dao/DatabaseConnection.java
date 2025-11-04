package nam.nam.dao;

import java.sql.*;
import java.sql.SQLException;

public class DatabaseConnection {
    private static String URL = "jdbc:mysql://localhost:3306/todo_db";
    private static String USER = "root";
    private static String PASS = "root";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
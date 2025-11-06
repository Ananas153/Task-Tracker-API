package nam.nam.dao;

import nam.nam.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private DatabaseConnection databaseConnection;
    private final String CREATE_USER = "INSERT INTO users (name,email,password) VALUES (?,?,?)";
    private final String CHECK_EMAIL_UNIQUE = "SELECT 1 FROM users WHERE email = ? LIMIT 1";

    public UserDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean createUser(User user) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            int rowInserted = preparedStatement.executeUpdate();
            return rowInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error: CREATE new user :" + e.getMessage());
            return false;
        }
    }

    // Return true if email already exists
    public boolean checkEmailExist(String email) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_EMAIL_UNIQUE)) {
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // True if 1 row is returned bu resultSet
        } catch (SQLException e) {
            System.err.println("Error: CHECK if email is unique :" + e.getMessage());
            return false;
        }
    }
}

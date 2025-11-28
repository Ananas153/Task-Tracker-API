package nam.nam.dao;

import nam.nam.exception.user.DatabaseException;
import nam.nam.model.User;

import java.sql.*;
import java.util.Optional;

public class UserDAO {
    private DatabaseConnection databaseConnection;
    private final String CREATE_USER = "INSERT INTO users (name,email,password) VALUES (?,?,?)";
    private final String CHECK_EMAIL_UNIQUE = "SELECT 1 FROM users WHERE email = ? LIMIT 1";
    private final String FIND_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

    public UserDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public int createUser(User user) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
            ResultSet resultset = preparedStatement.getGeneratedKeys();
            if(resultset.next()){
                return resultset.getInt(1);
            }else{
                throw new RuntimeException("User created but no ID returned.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error creating user", e);
        }
    }

    // Return true if email already exists
    public boolean checkEmailExist(String email) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_EMAIL_UNIQUE)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // True if 1 row is returned bu resultSet
        } catch (SQLException e) {
            System.err.println("Error: CHECK if email is unique :" + e.getMessage());
            return false;
        }
    }

    public Optional<User> findByEmail(String email) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String emailDb = resultSet.getString("email");
                String passHashed = resultSet.getString("password");
                return Optional.of(new User(id, name, emailDb, passHashed));
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error: FIND user by Email :" + e.getMessage());
            return Optional.empty();
        }
    }
}

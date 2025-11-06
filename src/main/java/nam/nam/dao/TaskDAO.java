package nam.nam.dao;

import nam.nam.model.Status;
import nam.nam.model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class TaskDAO {
    private DatabaseConnection databaseConnection;
    private final String GET_TASK_BY_ID = "SELECT * FROM tasks WHERE id = ?";

    public TaskDAO(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public Optional<Task> getTaskById(int id) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TASK_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int idUser = resultSet.getInt("idUser");
                String description = resultSet.getString("description");
                Status status = Status.valueOf(resultSet.getString("status"));
                LocalDateTime createdAt = resultSet.getTimestamp("createdAt").toLocalDateTime();
                LocalDateTime updatedAt = resultSet.getTimestamp("updatedAt").toLocalDateTime();

                Task task = new Task(id, idUser, description, status, createdAt, updatedAt);
                return Optional.of(task);
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error: GET task by ID : " + e.getMessage());
            return Optional.empty();
        }
    }
}

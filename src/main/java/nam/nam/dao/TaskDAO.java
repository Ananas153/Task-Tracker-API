package nam.nam.dao;

import nam.nam.model.Status;
import nam.nam.model.Task;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class TaskDAO {
    private DatabaseConnection databaseConnection;
    private final String GET_TASK_BY_ID = "SELECT * FROM tasks WHERE id = ?";
    private final String CREATE_TASK = "INSERT INTO tasks (idUser, description, status) VALUES (?,?,?)";
    private final String UPDATE_TASK = "UPDATE tasks SET description = ?, status = ? WHERE id = ? AND idUser = ?";

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

    public Optional<Integer> createTask(Task task) {
        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_TASK, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, task.getIdUser());
            statement.setString(2, task.getDescription());
            statement.setString(3, task.getStatus().toString());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int generatedId = resultSet.getInt(1);
                return Optional.of(generatedId);
            }
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("Error: CREATE new task : " + e.getMessage());
            return Optional.empty();
        }
    }

    // Il faut faire en sort que chaque nouvel task créé par un utilisateur doit avoir un nouvel
    // ID dans les tasks appartiennent à cet utilisateur. Pas incrémenter dans le tas de tasks
    // existants. 
    public boolean updateTask(Task task){
        try(Connection connection = databaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_TASK)){
            statement.setString(1,task.getDescription());
            statement.setString(2, task.getStatus().toString());
            statement.setInt(3,task.getId());
            statement.setInt(4, task.getIdUser());
            int rowAffected = statement.executeUpdate();
            return rowAffected > 0;
        }catch(SQLException e){
            System.err.println("Error: UPDATE existed task : " + e.getMessage());
            return false;
        }
    }
}

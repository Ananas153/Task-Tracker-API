package nam.nam.service;

import nam.nam.dao.TaskDAO;
import nam.nam.dto.taskDTO.TaskCreateDto;
import nam.nam.dto.taskDTO.TaskUpdateDto;
import nam.nam.model.Task;
import nam.nam.authentication.JwtAuthentication;

import java.util.Optional;

public class TaskService {
    private TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public Optional<Task> getTaskById(int id) {
        return taskDAO.getTaskById(id);
    }

    public Optional<Integer> createTask(String token, TaskCreateDto taskCreateDto){
        JwtAuthentication.validateToken(token);
        int userId = JwtAuthentication.extractUserIdFromToken(token);
        Task task = new Task(userId,taskCreateDto.description(),taskCreateDto.status());
        return taskDAO.createTask(task);
    }

    public boolean updateTask(String token, int taskId, TaskUpdateDto taskUpdateDto){
        JwtAuthentication.validateToken(token);
        int userId = JwtAuthentication.extractUserIdFromToken(token);
        Task task = new Task(taskId, userId, taskUpdateDto.description(), taskUpdateDto.status());
        return taskDAO.updateTask(task);
    }
}

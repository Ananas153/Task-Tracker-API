package nam.nam.service;

import nam.nam.dao.TaskDAO;
import nam.nam.dto.taskDTO.TaskCreateDto;
import nam.nam.model.Task;
import nam.nam.util.JwtUtil;

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
        JwtUtil.validateToken(token);
        int userId = JwtUtil.extractUserIdFromToken(token);
        Task task = new Task(userId,taskCreateDto.description(),taskCreateDto.status());
        return taskDAO.createTask(task);
    }
}

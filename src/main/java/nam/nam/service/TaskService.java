package nam.nam.service;

import nam.nam.dao.TaskDAO;
import nam.nam.model.Task;

import java.util.Optional;

public class TaskService {
    private TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public Optional<Task> getTaskById(int id) {
        return taskDAO.getTaskById(id);
    }
}

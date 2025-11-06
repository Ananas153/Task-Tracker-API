package nam.nam.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nam.nam.dao.DatabaseConnection;
import nam.nam.dao.TaskDAO;
import nam.nam.dto.TaskDto;
import nam.nam.mapper.TaskMapper;
import nam.nam.model.Task;
import nam.nam.service.TaskService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {
    private TaskService taskService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        this.taskService = new TaskService(new TaskDAO(new DatabaseConnection()));
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"id required\"}");
            return;
        }

        int id = Integer.parseInt(idParam);
        Optional<Task> opt = taskService.getTaskById(id);
        if (opt.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\":\"not found\"}");
            return;
        }

        TaskDto dto = TaskMapper.toDto(opt.get());
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getWriter(), dto);
    }
}

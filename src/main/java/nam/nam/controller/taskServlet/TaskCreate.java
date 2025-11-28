package nam.nam.controller.taskServlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nam.nam.dao.DatabaseConnection;
import nam.nam.dao.TaskDAO;
import nam.nam.dto.taskDTO.TaskCreateDto;
import nam.nam.exception.user.InvalidTokenException;
import nam.nam.service.TaskService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/tasks/create")
public class TaskCreate extends HttpServlet {
    private TaskService taskService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        this.taskService = new TaskService(new TaskDAO(new DatabaseConnection()));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Missing or invalid Authorization header\"}");
            return;
        }
        String token = authHeader.substring("Bearer ".length());

        TaskCreateDto taskCreateDto = objectMapper.readValue(request.getReader(), TaskCreateDto.class);
        try {
            Optional<Integer> optTaskId = taskService.createTask(token, taskCreateDto);
            if (optTaskId.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"not found\"}");
            }
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("{\"id\": " + optTaskId.get() +
                    ",\"description\":" + taskCreateDto.description() +
                    ",\"status\":" + taskCreateDto.status() + "}");
        } catch (InvalidTokenException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Session expired. Please log in again.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Something went wrong.\"}");
        }
    }
}

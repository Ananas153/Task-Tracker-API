package nam.nam.controller.taskServlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nam.nam.dao.DatabaseConnection;
import nam.nam.dao.TaskDAO;
import nam.nam.dto.taskDTO.TaskUpdateDto;
import nam.nam.exception.user.InvalidTokenException;
import nam.nam.service.TaskService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

//FIX the URL, doesn't respect the REST API architect -> Violating the Resource-based
//URL points to a thing(a task, a user) that we can do operation on to(Create, Delete, etc)
//not an action, so NO weird verb in side the URL
@WebServlet("/tasks/update")
public class TaskUpdate extends HttpServlet {
    private TaskService taskService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        this.taskService = new TaskService(new TaskDAO(new DatabaseConnection()));
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Missing or invalid Authorization header\"}");
            return;
        }
        String token = authHeader.substring("Bearer ".length());

        String taskIdParam = request.getParameter("id");
        if (taskIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"id required\"}");
            return;
        }

        TaskUpdateDto taskUpdateDto = objectMapper.readValue(request.getReader(), TaskUpdateDto.class);
        try {
            boolean checkUpdate = taskService.updateTask(token, Integer.parseInt(taskIdParam), taskUpdateDto);
            if (!checkUpdate) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.getWriter().write("{\"error\":\"not modified\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"id\": " + taskIdParam +
                        ",\"description\":" + taskUpdateDto.description() +
                        ",\"status\":" + taskUpdateDto.status() + "}");
            }
        } catch (InvalidTokenException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Session expired. Please log in again.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Something went wrong.\"}");
        }
    }
}

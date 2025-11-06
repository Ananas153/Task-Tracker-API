package nam.nam.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nam.nam.dao.DatabaseConnection;
import nam.nam.dao.UserDAO;
import nam.nam.dto.UserCreateDto;
import nam.nam.exception.user.EmailAlreadyExistsException;
import nam.nam.service.UserService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private UserService userService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        this.userService = new UserService(new UserDAO(new DatabaseConnection()));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserCreateDto userCreateDto = objectMapper.readValue(request.getReader(), UserCreateDto.class);
        try {
            String token = userService.createUser(userCreateDto);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            response.getWriter().write("{\"token\":\"" + token + "\"}");

        } catch (EmailAlreadyExistsException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("{\"error\":\"Email already exists\"}");
        }
    }
}

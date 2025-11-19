package nam.nam.controller.userServlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nam.nam.dao.DatabaseConnection;
import nam.nam.dao.UserDAO;
import nam.nam.dto.UserLoginDto;
import nam.nam.exception.user.InvalidCredentialException;
import nam.nam.exception.user.UserExistException;
import nam.nam.service.UserService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@WebServlet("/users/login")
public class UserLogin extends HttpServlet {
    private UserService userService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() {
        this.userService = new UserService(new UserDAO(new DatabaseConnection()));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLoginDto userLoginDto = objectMapper.readValue(request.getReader(), UserLoginDto.class);
        try {
            String token = userService.userLogin(userLoginDto);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write("{\"token\":\"" + token + "\"}");
        } catch (UserExistException e) {
            System.err.println(e);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Account doesn't exists!\"}");
        } catch (InvalidCredentialException e) {
            System.err.println(e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Invalid Credential!\"}");
        }
    }
}

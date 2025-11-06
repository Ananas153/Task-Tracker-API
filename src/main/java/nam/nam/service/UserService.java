package nam.nam.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import nam.nam.dao.UserDAO;
import nam.nam.dto.UserCreateDto;
import nam.nam.exception.user.EmailAlreadyExistsException;
import nam.nam.model.User;

import java.util.Date;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    //Check business logic & validation rules
    public String createUser(UserCreateDto userCreateDto){
        if(userDAO.checkEmailExist(userCreateDto.email())){
            throw new EmailAlreadyExistsException(userCreateDto.email());
        }
        String hashedPass = BCrypt.withDefaults().hashToString(12,userCreateDto.password().toCharArray());
        User user = new User(userCreateDto.name(), userCreateDto.email(), hashedPass);
        userDAO.createUser(user);

        // Generate JWT token
        String token = JWT.create()
                .withSubject(userCreateDto.email())
                .withClaim("name", userCreateDto.name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .sign(Algorithm.HMAC256("your-secret-key")); // Replace with your actual secret key
        return token;
    }
}

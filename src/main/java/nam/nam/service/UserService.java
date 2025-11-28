package nam.nam.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import nam.nam.dao.UserDAO;
import nam.nam.dto.userDTO.UserCreateDto;
import nam.nam.dto.userDTO.UserLoginDto;
import nam.nam.exception.user.EmailAlreadyExistsException;
import nam.nam.exception.user.InvalidCredentialException;
import nam.nam.exception.user.UserExistException;
import nam.nam.model.User;
import nam.nam.util.JwtUtil;

import java.util.Optional;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    //Check business logic & validation rules
    public String createUser(UserCreateDto userCreateDto) {
        if (userDAO.checkEmailExist(userCreateDto.email())) {
            throw new EmailAlreadyExistsException(userCreateDto.email());
        }
        String hashedPass = BCrypt.withDefaults().hashToString(12, userCreateDto.password().toCharArray());
        User user = new User(userCreateDto.name(), userCreateDto.email(), hashedPass);
        int userId = userDAO.createUser(user);

        return JwtUtil.JwtUtil(userCreateDto.email(),userId);
    }

    public String userLogin(UserLoginDto userLoginDto) {
        Optional<User> optUser = userDAO.findByEmail(userLoginDto.email());
        if (optUser.isEmpty()) {
            throw new UserExistException();
        }
        BCrypt.Result result = BCrypt.verifyer().verify(userLoginDto.password().toCharArray(), optUser.get().getPassword());
        if (!result.verified) {
            throw new InvalidCredentialException();
        }
        return JwtUtil.JwtUtil(userLoginDto.email(),optUser.get().getId());
    }
}

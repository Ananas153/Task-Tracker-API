package nam.nam.exception.user;

public class UserExistException extends RuntimeException {
    public UserExistException() {
        super("Account doesn't exist!");
    }
}


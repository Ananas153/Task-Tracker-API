package nam.nam.exception.user;

public class InvalidCredentialException extends RuntimeException {
    public InvalidCredentialException() {
        super("Invalid credentials");
    }
}

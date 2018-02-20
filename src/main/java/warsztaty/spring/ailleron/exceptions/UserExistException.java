package warsztaty.spring.ailleron.exceptions;

public class UserExistException extends Exception {
    public UserExistException(String message) {
        super(message);
    }
}

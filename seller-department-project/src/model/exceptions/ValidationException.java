package model.exceptions;

public class ValidationException extends DBException {
    public ValidationException(String msg){
        super(msg);
    }

    public ValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

package model.exceptions;

public class UniqueViolationException extends DBException {
    public UniqueViolationException(String msg) {
        super(msg);
    }

    public UniqueViolationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
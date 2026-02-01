package model.exceptions;

public class DBException extends RuntimeException {
    public DBException(String msg) {
        super(msg);
    }

    public DBException(String msg, Throwable clause) {
        super(msg, clause);
    }
}

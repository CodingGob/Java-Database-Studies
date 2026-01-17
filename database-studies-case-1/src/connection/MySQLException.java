package connection;

import java.sql.SQLException;

public class MySQLException extends SQLException {

    public MySQLException(String message) {
        super(message);
    }

    public MySQLException(String message, Throwable cause) {
        super(message, cause);
    }

    public MySQLException(String message, String sqlState) {
        super(message, sqlState);
    }

    public MySQLException(String message, String sqlState, int vendorCode) {
        super(message, sqlState, vendorCode);
    }

    public MySQLException(String message, String sqlState, int vendorCode, Throwable cause) {
        super(message, sqlState, vendorCode, cause);
    }
}
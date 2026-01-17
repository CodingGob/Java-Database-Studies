package connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnection {

    private static Connection conn = null;

    private static Properties loadProperties() throws MySQLException {
        Properties props = new Properties();

        try (InputStream is = MySQLConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (is == null) {
                throw new MySQLException("db.properties not found in classpath");
            }

            props.load(is);

        } catch (Exception e) {
            throw new MySQLException("Could not load db.properties", e);
        }

        return props;
    }

    public static Connection getConnection() throws MySQLException {
        try {
            if (conn == null || conn.isClosed()) {
                Properties props = loadProperties();
                String url = props.getProperty("db.url");
                conn = DriverManager.getConnection(url, props);
            }
            return conn;
        } catch (SQLException e) {
            throw new MySQLException("Could not get database connection", e);
        }
    }

    public static void closeConnection() throws MySQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new MySQLException("Could not close connection", e);
        }
    }
}

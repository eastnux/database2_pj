package mobile.banking.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionUtil {
    public static final String URL =
            "jdbc:postgresql://mobile.wisoft.dev:10002/donggun";
    public static final String USERNAME = "donggun";
    public static final String PASSWORD = "donggun";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

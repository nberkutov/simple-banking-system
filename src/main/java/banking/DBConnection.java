package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private final Connection connection;

    private DBConnection(String databaseUrl) throws SQLException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(databaseUrl);
        connection = dataSource.getConnection();
    }

    public static DBConnection getInstance(String databaseUrl) throws SQLException {
        if (instance == null) {
            instance = new DBConnection(databaseUrl);
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

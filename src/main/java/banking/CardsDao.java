package banking;

import java.sql.*;

public class CardsDao implements AutoCloseable {

    private static final String createIfNotExistsSql =
            "CREATE TABLE IF NOT EXISTS card (" +
            "id INTEGER," +
            "number TEXT," +
            "pin TEXT," +
            "balance INTEGER DEFAULT 0)";

    private static final String selectCountAllSql = "SELECT COUNT(*) FROM card";
    private static final String insertCardSql = "INSERT INTO card VALUES(?, ?, ?, ?)";
    private static final String countNumberAndPinSql = "SELECT COUNT(*) FROM card WHERE number = ? AND pin = ?";
    private static final String selectBalanceByNumberSql = "SELECT balance FROM card WHERE number = ?";
    private static final String setBalanceByNumberSql = "UPDATE card SET balance = ? WHERE number = ?";
    private static final String countCardWithNumberSql = "SELECT COUNT(*) FROM card WHERE number = ?";
    private static final String deleteRowByNumberSql = "DELETE FROM card WHERE number = ?";

    private final Connection connection;

    public CardsDao(Connection connection) throws SQLException{
        this.connection = connection;
    }

    public void createCardsTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                    "id INTEGER," +
                    "number TEXT," +
                    "pin TEXT," +
                    "balance INTEGER DEFAULT 0)");
        }
    }

    public int insertNewCard(String cardNumber, String pin) throws SQLException {
        int rowsAffected = 0;
        int id = 0;
        try (PreparedStatement statement = connection.prepareStatement(selectCountAllSql)){
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                id = set.getInt(1);
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(insertCardSql)) {
            statement.setInt(1, id);
            statement.setString(2, cardNumber);
            statement.setString(3, pin);
            statement.setInt(4, 0);
            rowsAffected = statement.executeUpdate();
        }
        return rowsAffected;
    }

    public boolean containsCard(String card) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(countCardWithNumberSql)){
            statement.setString(1, card);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean validatePin(String cardNumber, String pin) throws SQLException {
        boolean isValid = false;
        try (PreparedStatement statement = connection.prepareStatement(countNumberAndPinSql)) {
            statement.setString(1, cardNumber);
            statement.setString(2, pin);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                isValid = set.getInt(1) > 0;
            }
        }
        return isValid;
    }

    public int getBalance(String cardNumber) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(selectBalanceByNumberSql)){
            statement.setString(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("balance");
            }
        }
        return -1;
    }

    public int updateBalance(String cardNumber, int balance) throws SQLException {
        int rowsAffected = 0;
        try (PreparedStatement statement = connection.prepareStatement(setBalanceByNumberSql)) {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }

            statement.setInt(1, balance);
            statement.setString(2, cardNumber);
            rowsAffected = statement.executeUpdate();
            connection.commit();

            connection.setAutoCommit(true);
        } catch (SQLException e) {
            connection.rollback();
        }
        return rowsAffected;
    }

    public void transfer(String sender, String receiver, int amount) throws SQLException {
        if (connection.getAutoCommit()) {
            connection.setAutoCommit(false);
        }

        try (PreparedStatement getBalanceStmt = connection.prepareStatement(selectBalanceByNumberSql);
             PreparedStatement updateBalanceStmt = connection.prepareStatement(setBalanceByNumberSql)) {
            Savepoint savepoint = connection.setSavepoint();

            int senderBalance = 0;

            getBalanceStmt.setString(1, sender);
            ResultSet resultSet = getBalanceStmt.executeQuery();
            if (resultSet.next()) {
                senderBalance = resultSet.getInt(1);
            }

            updateBalanceStmt.setInt(1, senderBalance - amount);
            updateBalanceStmt.setString(2, sender);
            updateBalanceStmt.executeUpdate();

            int receiverBalance = 0;
            getBalanceStmt.setString(1, receiver);
            resultSet = getBalanceStmt.executeQuery();
            if (resultSet.next()) {
                receiverBalance = resultSet.getInt(1);
            }

            updateBalanceStmt.setInt(1, receiverBalance + amount);
            updateBalanceStmt.setString(2, receiver);
            updateBalanceStmt.executeUpdate();

            connection.commit();

            connection.setAutoCommit(true);
        }
    }

    public int delete(String cardNumber) throws SQLException {
        int rowsAffected = 0;
        try (PreparedStatement statement = connection.prepareStatement(deleteRowByNumberSql)) {
            statement.setString(1, cardNumber);
            rowsAffected = statement.executeUpdate();
        }
        return rowsAffected;
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}

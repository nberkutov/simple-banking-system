package banking.command;

import banking.BankAccountException;

import java.sql.SQLException;

public interface Command {
    void execute() throws SQLException, BankAccountException;
}

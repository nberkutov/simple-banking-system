package banking.command;

import banking.BankAccountException;
import banking.command.Command;

import java.sql.SQLException;

public class Controller {
    private Command command;
    private boolean exited;
    private boolean loggedIn;

    public Controller() {
        exited = false;
        loggedIn = false;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() throws SQLException, BankAccountException {
        if (exited) {
            return;
        }
        command.execute();
    }

    public void exit() {
        exited = true;
    }

    public boolean isExited() {
        return exited;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}

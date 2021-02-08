package banking.command;

import banking.BankAccountException;
import banking.CardsDao;

import java.sql.SQLException;
import java.util.Scanner;

public class LoginCommand implements Command {
    private final Controller controller;
    private final CardsDao cardsDao;

    public LoginCommand(Controller controller, CardsDao cardsDao) {
        this.controller = controller;
        this.cardsDao = cardsDao;
    }

    @Override
    public void execute() throws SQLException, BankAccountException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String PIN = scanner.nextLine();
        System.out.println();
        if (!cardsDao.validatePin(cardNumber, PIN)) {
            System.out.println("Wrong card number or PIN!");
            return;
        }
        System.out.println("You have successfully logged in!\n");
        controller.setLoggedIn(true);
        controller.setCommand(new ShowAccountMenuCommand(controller, cardsDao, cardNumber));
        controller.executeCommand();
    }
}

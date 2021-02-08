package banking.command;

import banking.BankAccountException;
import banking.CardsDao;

import java.sql.SQLException;
import java.util.Scanner;

public class ShowStartMenuCommand implements Command {
    private final Controller controller;
    private final CardsDao cardsDao;

    public ShowStartMenuCommand(Controller controller, CardsDao cardsDao) {
        this.controller = controller;
        this.cardsDao = cardsDao;
    }

    @Override
    public void execute() throws SQLException, BankAccountException {
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        while (!controller.isExited() && input != 0) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            input = scanner.nextInt();
            System.out.println();
            switch (input) {
                case 1:
                    controller.setCommand(new CreateNewCardCommand(cardsDao));
                    break;
                case 2:
                    controller.setCommand(new LoginCommand(controller, cardsDao));
                    break;
                case 0:
                    controller.exit();
            }
            controller.executeCommand();
        }
    }
}

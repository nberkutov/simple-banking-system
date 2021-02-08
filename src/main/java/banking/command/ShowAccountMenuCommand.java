package banking.command;

import banking.BankAccountException;
import banking.CardsDao;

import java.sql.SQLException;
import java.util.Scanner;

public class ShowAccountMenuCommand implements Command {
    private final Controller controller;
    private final CardsDao cardsDao;
    private final String cardNumber;

    public ShowAccountMenuCommand(Controller controller, CardsDao cardsDao, String cardNumber) {
        this.controller = controller;
        this.cardsDao = cardsDao;
        this.cardNumber = cardNumber;
    }

    @Override
    public void execute() throws SQLException, BankAccountException {
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        while (controller.isLoggedIn() && !controller.isExited()) {
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            input = scanner.nextInt();
            System.out.println();
            switch (input) {
                case 1:
                    controller.setCommand(new ShowBalanceCommand(cardsDao, cardNumber));
                    break;
                case 2:
                    controller.setCommand(new AddIncomeCommand(cardsDao, cardNumber));
                    break;
                case 3:
                    controller.setCommand(new TransferCommand(cardsDao, cardNumber));
                    break;
                case 4:
                    controller.setLoggedIn(false);
                    controller.setCommand(new CloseAccountCommand(cardsDao, cardNumber));
                    break;
                case 5:
                    controller.setCommand(new ShowStartMenuCommand(controller, cardsDao));
                    return;
                case 0:
                    controller.exit();
                    return;
            }
            try {
                controller.executeCommand();
            } catch (BankAccountException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

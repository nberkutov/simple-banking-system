package banking.command;

import banking.CardsDao;

import java.sql.SQLException;
import java.util.Scanner;

public class AddIncomeCommand implements Command {
    private final CardsDao cardsDao;
    private final String cardNumber;

    public AddIncomeCommand(CardsDao cardsDao, String cardNumber) {
        this.cardsDao = cardsDao;
        this.cardNumber = cardNumber;
    }

    @Override
    public void execute() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter income:");
        int balance = cardsDao.getBalance(cardNumber);
        int income = scanner.nextInt();
        balance += income;
        int rowsAffected = cardsDao.updateBalance(cardNumber, balance);
        if (rowsAffected > 0) {
            System.out.println("Income was added!\n");
        }
    }
}

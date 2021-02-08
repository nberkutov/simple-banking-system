package banking;

import banking.command.Controller;
import banking.command.ShowStartMenuCommand;
import org.sqlite.SQLiteDataSource;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String IIN = "400000";

    private static boolean exitWasCalled = false;
    private static DBConnection dbConnection;

    public static void main(String[] args) {
        Controller controller = new Controller();
        if (args.length < 2) {
            System.out.println("Specify a database file.");
            return;
        }
        String dbUrl = "jdbc:sqlite:" + args[1];
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(dbUrl);
        try {
            dbConnection = DBConnection.getInstance(dbUrl);
            try (CardsDao cardsDao = new CardsDao(dbConnection.getConnection())) {
                cardsDao.createCardsTable();
                controller.setCommand(new ShowStartMenuCommand(controller, cardsDao));
                controller.executeCommand();
            }
        } catch (SQLException|BankAccountException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void showStartMenu(CardsDao cardsDao)  throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        while (input != 0 && !exitWasCalled) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            input = scanner.nextInt();
            System.out.println();
            switch (input) {
                case 1:
                    generateCard(cardsDao);
                    break;
                case 2:
                    loginMenu(cardsDao);
                    break;
            }
        }
    }

    public static void generateCard(CardsDao cardsDao) throws SQLException {
        String cardNumber = AccountService.generateCardNumber();
        String cardPIN = AccountService.generatePIN();

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(cardPIN);
        System.out.println();

        cardsDao.insertNewCard(cardNumber, cardPIN);
    }

    public static void loginMenu(CardsDao cardsDao) throws SQLException {
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
        accountMenu(cardsDao, cardNumber);
    }

    private static void accountMenu(CardsDao cardsDao, String account) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int input = -1;
        while (true) {
            System.out.println("1. Balance");
            System.out.println("2. Log out");
            System.out.println("0. Exit");
            input = scanner.nextInt();
            System.out.println();
            switch (input) {
                case 1:
                    showBalance(cardsDao, account);
                    break;
                case 2:
                    return;
                case 0:
                    exitWasCalled = true;
                    return;
            }
        }
    }

    private static void showBalance(CardsDao cardsDao, String account) throws SQLException {
        System.out.printf("Balance: %d\n\n", cardsDao.getBalance(account));
    }
}
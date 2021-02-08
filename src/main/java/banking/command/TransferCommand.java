package banking.command;

import banking.AccountErrorCode;
import banking.AccountService;
import banking.BankAccountException;
import banking.CardsDao;

import java.sql.SQLException;
import java.util.Scanner;

public class TransferCommand implements Command {
    private final CardsDao cardsDao;
    private final String cardNumber;

    public TransferCommand(CardsDao cardsDao, String cardNumber) {
        this.cardsDao = cardsDao;
        this.cardNumber = cardNumber;
    }

    @Override
    public void execute() throws SQLException, BankAccountException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter card number:");
        String receiver = scanner.nextLine();
        if (!AccountService.isValidCardNumber(receiver)) {
            throw new BankAccountException(AccountErrorCode.INVALID_CARD_NUMBER);
        }
        if (!cardsDao.containsCard(receiver)) {
            throw new BankAccountException(AccountErrorCode.CARD_DOES_NOT_EXIST);
        }

        System.out.println("Enter how much money you want to transfer:");
        int transferAmount = scanner.nextInt();
        int balance = cardsDao.getBalance(cardNumber);

        if (transferAmount > balance) {
            throw new BankAccountException(AccountErrorCode.NOT_ENOUGH_MONEY);
        }

        cardsDao.transfer(cardNumber, receiver, transferAmount);
        System.out.println("Success!\n");
    }
}
//2000007269641768
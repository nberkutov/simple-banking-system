package banking.command;

import banking.AccountService;
import banking.CardsDao;

import java.sql.SQLException;

public class CreateNewCardCommand implements Command {
    private final CardsDao cardsDao;

    public CreateNewCardCommand(CardsDao cardsDao) {
        this.cardsDao = cardsDao;
    }

    @Override
    public void execute() throws SQLException {
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
}

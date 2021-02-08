package banking.command;

import banking.CardsDao;

import java.sql.SQLException;

public class ShowBalanceCommand implements Command {
    private final CardsDao cardsDao;
    private final String cardNumber;

    public ShowBalanceCommand(CardsDao cardsDao, String cardNumber) {
        this.cardsDao = cardsDao;
        this.cardNumber = cardNumber;
    }

    @Override
    public void execute() throws SQLException {
        System.out.printf("Balance: %d\n\n", cardsDao.getBalance(cardNumber));
    }
}

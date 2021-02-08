package banking.command;

import banking.CardsDao;

import java.sql.SQLException;

public class CloseAccountCommand implements Command {
    private final CardsDao cardsDao;
    private final String cardNumber;

    public CloseAccountCommand(CardsDao cardsDao, String cardNumber) {
        this.cardsDao = cardsDao;
        this.cardNumber = cardNumber;
    }

    @Override
    public void execute() throws SQLException {
        if (cardsDao.delete(cardNumber) > 0) {
            System.out.println("The account has been closed!\n");
        }
    }
}

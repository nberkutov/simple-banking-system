package banking;

public enum AccountErrorCode {
    WRONG_NUMBER_OR_PIN("\nWrong card number or PIN!\n"),
    CARD_DOES_NOT_EXIST("\nSuch a card does not exist.\n"),
    NOT_ENOUGH_MONEY("\nNot enough money!\n"),
    INVALID_CARD_NUMBER("\nProbably you made a mistake in the card number. Please try again!\n");

    private String message;

    AccountErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

package banking;

public class BankAccountException extends Exception {
    private AccountErrorCode errorCode;

    public BankAccountException(AccountErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
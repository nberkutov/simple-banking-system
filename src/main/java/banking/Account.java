package banking;

public class Account {
    private final String number;
    private final String pin;


    public Account(String number, String pin) {
        this.number = number;
        this.pin = pin;
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }
}

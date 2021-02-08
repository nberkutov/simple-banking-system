package banking;

import java.util.Random;

public class AccountService {
    private static final Random random = new Random(17);
    private static final String IIN = "400000";

    private static int checkSum(String number) {
        int sum = 0;
        for (int i = 0; i < 15; i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }
        return sum;
    }

    public static String generateCardNumber() {
        int accountId = random.nextInt(1000000000);
        String cardNumber = IIN + String.format("%09d", accountId);
        int checkSum = checkSum(cardNumber);
        int lastDigit = 10 - checkSum % 10;
        if (lastDigit >= 10) {
            lastDigit -= 10;
        }
        cardNumber += lastDigit;
        return cardNumber;
    }

    public static String generatePIN() {
        return String.format("%04d", random.nextInt(10000));
    }

    public static boolean isValidCardNumber(String number) {
        return checkLuhnAlgorithm(number);
    }

    private static boolean checkLuhnAlgorithm(String cardNumber) {
        int result = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (i % 2 == 0) {
                int doubleDigit = digit * 2 > 9 ? digit * 2 - 9 : digit * 2;
                result += doubleDigit;
                continue;
            }
            result += digit;
        }
        return result % 10 == 0;
    }
}

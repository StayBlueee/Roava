import artifacts.account.Account;
import artifacts.exceptions.AccountException;

public class Main {
    public static void main(String[] args) throws Exception {
        Account account;

        try {
            account = new Account("");
        } catch (AccountException e) {
            System.out.println(e.getMessage());

            return;
        }

        System.out.println("Logged in as " + account.getUserName() + " with user ID " + account.getUserId());
    }
}
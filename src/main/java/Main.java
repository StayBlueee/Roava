import artifacts.account.Account;
import artifacts.exceptions.AccountException;
import artifacts.groups.Group;
import artifacts.user.User;

public class Main {
    public static void main(String[] args) {
        Account account;
try {
            account = new Account("cookie");
        } catch (AccountException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Logged in as " + account.getUserName() + " with user ID " + account.getUserId());
    }
}
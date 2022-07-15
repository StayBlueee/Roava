import artifacts.account.Account;
import artifacts.exceptions.TokenNotValidException;
import artifacts.groups.Group;
import artifacts.user.User;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        Account account;

        try {
            account = new Account("");
        } catch (TokenNotValidException e) {
            System.out.println(e.getMessage());

            return;
        }

        System.out.println("Logged in as " + account.getUserName() + " with user ID " + account.getUserId());
    }
}
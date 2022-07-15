import artifacts.account.Account;
import artifacts.exceptions.TokenNotValidException;
import artifacts.groups.Group;
import artifacts.user.User;
import coresearch.cvurl.io.model.Response;
import misc.Requester;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Account account;

        try {
            account = new Account("");
        } catch (TokenNotValidException e) {
            System.out.println(e.getMessage());
            return;
        }

        User user = new User(1);

        ArrayList<Group> groups = user.getGroups();

        for (Group group : groups) {
            System.out.println(group.getId() + " " + group.getName());
        }

        System.out.println("Logged in as " + account.getUserName() + " with user ID " + account.getUserId());
    }
}
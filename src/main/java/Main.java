import artifacts.client.Client;
import artifacts.exceptions.AccountException;
import artifacts.exceptions.GroupException;
import artifacts.exceptions.RequestException;
import artifacts.groups.Group;

public class Main {
    public static void main(String[] args) {
        Client client;

        try {
            client = new Client("");
        } catch (AccountException e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            Group group = new Group(1);

            group.rankUser(1, "Guest");

            System.out.println("Successfully ranked user!");
        } catch (GroupException e) {
            System.out.println(e.getMessage());

            return;
        } catch (RequestException e) {
            System.out.println(e.getMessage());

            return;
        }

        //System.out.println("Logged in as " + account.getUserName() + " with user ID " + account.getUserId());
    }
}
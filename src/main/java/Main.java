import artifacts.account.Account;
import artifacts.exceptions.TokenNotValidException;
import artifacts.exceptions.UserIdNotValid;
import artifacts.user.User;

public class Main {
    public static void main(String[] args) throws TokenNotValidException, UserIdNotValid {
        Account account = new Account();
        account.setAccount(null);
        User user = new User("623426");
        System.out.println(user.getUsername());
    }
}

package artifacts.account;

import artifacts.exceptions.TokenNotValidException;
import artifacts.user.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import misc.Requester;

public class Account {
    private String userName = "";
    private long userId = 0;

    public Requester requester = null;
    public String xcsrfToken = "";
    public String token = "";

    public String getUserName() {
        return userName;
    }

    public long getUserId() {
        return userId;
    }

    public Account(String token) throws TokenNotValidException {
        if (!token.toLowerCase().startsWith("_|warning:-")) {
            System.out.println("Your ROBLOSECURITY is not set properly. Please make sure that you include the entirety of the string, including the _|WARNING:-");

            return;
        }

        this.token = token;
        this.requester = new Requester(this);

        User user = new User("3oq");

        try {
            ObjectNode result = this.requester.sendRequestJson("https://www.roblox.com/my/profile", "GET", "");

            this.userName = result.get("Username").asText();
            this.userId = result.get("UserId").asLong();
        } catch (Exception exception) {
            throw new TokenNotValidException("The token you have provided is invalid!");
        }
    }
}
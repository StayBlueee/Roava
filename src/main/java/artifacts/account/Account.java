package artifacts.account;

import artifacts.exceptions.TokenNotValidException;
import artifacts.user.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import misc.Requester;

public class Account {
    private String userName = "";
    private long userId = 0;

    public String xcsrfToken = "";
    public static String token = "";


    public long getUserId() {
        return userId;
    }

    public void setAccount(String cookie) throws TokenNotValidException {
        Requester req = new Requester();
        Account.token = cookie;
        if (!token.toLowerCase().startsWith("_|warning:-")) {
            System.out.println("Your ROBLOSECURITY is not set properly. Please make sure that you include the entirety of the string, including the _|WARNING:-");
            return;
        }
        req.setAccount(this);
        try {
            ObjectNode result = req.sendRequestJSON("https://www.roblox.com/my/profile", "GET", null);
            this.userName = result.get("Username").asText();
            this.userId = result.get("UserId").asLong();
        } catch (Exception exception) {
            throw new TokenNotValidException("The token you have provided is invalid!");
        }
    }


}
package artifacts.client;

import artifacts.exceptions.AccountException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import misc.Requester;

public class Client {
    private String userName;
    private long userId;
    public Requester requester;
    public String cookie;
    public String token = "";

    public String getUserName() {
        return userName;
    }

    public long getUserId() {
        return userId;
    }

    public Client(String cookie) throws AccountException {
        if (!cookie.toLowerCase().startsWith("_|warning:-")) {
            throw new AccountException("Your .ROBLOSECURITY is not set properly. Please make sure that you include the entirety of the string, including the _|WARNING:-");
        }

        this.cookie = cookie;
        this.requester = new Requester(this);

        try {
            ObjectNode result = this.requester.sendRequestJson("https://www.roblox.com/my/profile", "GET", "");

            this.userName = result.get("Username").asText();
            this.userId = result.get("UserId").asLong();
        } catch (Exception exception) {
            throw new AccountException("The .ROBLOSECURITY you have provided is invalid!");
        }
    }
}
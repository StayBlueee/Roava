package artifacts.account;

import artifacts.exceptions.TokenNotValidException;
import misc.Requester;
import okhttp3.*;

import java.io.IOException;

public class Account {
    private OkHttpClient client = new OkHttpClient().newBuilder()
            .followRedirects(false)
            .build();

    private MediaType json = MediaType.get("application/json; charset=utf-8");

    private int retries = 0;

    public String xcsrfToken = "";
    public String token = "";

    public String getUserName() {
        return Requester.sendRequestWithBody("https://www.roblox.com/my/profile", "GET", null).get("Username").getAsString();
    }

    public int getUserId() {
        return Requester.sendRequestWithBody("https://www.roblox.com/my/profile", "GET", null).get("UserID").getAsInt();
    }

    public Response authenticate() throws TokenNotValidException {
        Response response = Requester.sendRequest("https://auth.roblox.com/v2/login", "POST", RequestBody.create("", json));

        if (response.code() == 403) {
            if (retries < 2) {
                if (response.header("x-csrf-token") != null) {
                    this.xcsrfToken = response.header("x-csrf-token");
                }

                retries++;

                return authenticate();
            } else {
                throw new TokenNotValidException("Yes");
            }
        }

        return response;
    }

    public Account(String token) throws TokenNotValidException {
        if (!token.toLowerCase().startsWith("_|warning:-")) {
            System.out.println("Your ROBLOSECURITY is not set properly. Please make sure that you include the entirety of the string, including the _|WARNING:-");

            return;
        }

        this.token = token;

        Requester.setAccount(this);

        Response response = Requester.sendRequest("https://www.roblox.com/my/profile", "GET", null);

        if (!response.header("content-type").equals("application/json; charset=utf-8")) {
            throw new TokenNotValidException("The token you have provided is invalid!");
        }

        try {
            System.out.println(response.body().string());
        } catch (IOException e) {

        }

        try {
            response = authenticate();
        } catch(TokenNotValidException e) {
            System.out.println(e);

            return;
        }

        System.out.println(response.code());
    }
}
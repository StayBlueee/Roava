package artifacts.account;

import misc.Requester;
import okhttp3.*;

public class Account {
    private OkHttpClient client = new OkHttpClient().newBuilder()
            .followRedirects(false)
            .build();;

    private MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public String robloSecurity = "";
    public String xsrfToken = "";

    public String getUserName() {
        return Requester.sendRequestWithBody("https://www.roblox.com/mobileapi/userinfo", "GET", null, this).get("UserName").getAsString();
    }

    public int getUserId() {
        Requester.sendRequestWithBody("https://www.roblox.com/mobileapi/userinfo", "GET", null, this).get("UserID").getAsInt();

        return 1;
    }

    public Response authenticate() {
        return Requester.sendRequest("https://auth.roblox.com/v2/login", "POST", RequestBody.create("", JSON), this);
    }

    public Account(String robloSecurity) {
        if (!robloSecurity.toLowerCase().startsWith("_|warning:-")) {
            System.out.println("Your ROBLOSECURITY is not set properly. Please make sure that you include the entirety of the string, including the _|WARNING:-");

            return;
        }

        this.robloSecurity = robloSecurity;

        Response response = authenticate();

        System.out.println(response.code());

        if (response.header("x-csrf-token") != null) {
            this.xsrfToken = response.header("x-csrf-token");
        }
    }
}

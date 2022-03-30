package artifacts.account;

import okhttp3.*;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class Account {
    private OkHttpClient client;

    private MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private String robloSecurity;
    private String xsrfToken;

    public String sendRequest(String url, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-CSRF-TOKEN", this.xsrfToken)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", ".ROBLOSECURITY=" + this.robloSecurity)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account(String robloSecurity) {
        this.client = new OkHttpClient().newBuilder()
                .followRedirects(false)
                .build();

        RequestBody body = RequestBody.create("", JSON);

        Request request = new Request.Builder()
                .url("https://auth.roblox.com/v2/login")
                .addHeader("Cookie", ".ROBLOSECURITY=" + robloSecurity)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            this.xsrfToken = response.header("x-csrf-token");
            this.robloSecurity = robloSecurity;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

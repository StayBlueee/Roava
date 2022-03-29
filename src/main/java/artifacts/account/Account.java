package artifacts.account;

import okhttp3.*;

import java.io.IOException;

public class Account {
    private OkHttpClient client = new OkHttpClient();

    private MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public String xsrfToken;

    public Response sendRequest(String url, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-CSRF-TOKEN", xsrfToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response;
        } catch (IOException e) {
            System.out.println("An error occurred while attempting to send your request!");
        }
    }

    public Account(String robloSecurity) {
        RequestBody body = RequestBody.create("", JSON);

        Request request = new Request.Builder()
                .url("https://auth.roblox.com/v2/login")
                .addHeader("Cookie", robloSecurity)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            this.xsrfToken = response.header("X-CSRF-TOKEN");
        } catch (IOException e) {
            System.out.println("An error occurred while attempting to send your request!");
        }
    }
}

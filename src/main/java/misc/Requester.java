package misc;

import artifacts.account.Account;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import okhttp3.Request.Builder;

import java.io.IOException;

public class Requester {
    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .followRedirects(false)
            .build();

    public static Builder requestBuilder(String url, String method, RequestBody body) {
        Builder builder = new Builder()
                .addHeader("Content-Type", "application/json")
                .url(url);

        if (method == "POST") {
            builder.post(body);
        } else if (method == "GET") {
            builder.get();
        }

        return builder;
    }

    public static Response sendRequest(String url, String method, RequestBody body, Account account) {
        Builder builder = requestBuilder(url, method, body);

        if (account != null) {
            if (account.robloSecurity != "") {
                builder.addHeader("Cookie", ".ROBLOSECURITY=" + account.robloSecurity);
            }

            if (account.xsrfToken != "") {
                builder.addHeader("X-CSRF-TOKEN", account.xsrfToken);
            }
        }

        try (Response response = client.newCall(builder.build()).execute()) {
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonObject sendRequestWithBody(String url, String method, RequestBody body, Account account) {
        Builder builder = requestBuilder(url, method, body);

        if (account != null) {
            if (account.robloSecurity != "") {
                builder.addHeader("Cookie", ".ROBLOSECURITY=" + account.robloSecurity);
            }

            if (account.xsrfToken != "") {
                builder.addHeader("X-CSRF-TOKEN", account.xsrfToken);
            }
        }

        try (Response response = client.newCall(builder.build()).execute()) {
            return JsonParser.parseString(response.body().string()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
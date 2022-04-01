package misc;

import artifacts.account.Account;
import com.fasterxml.jackson.databind.node.ObjectNode;
import coresearch.cvurl.io.model.Response;
import coresearch.cvurl.io.request.CVurl;
import coresearch.cvurl.io.request.RequestBuilder;


import java.util.Map;
import java.util.Optional;

public class Requester {
    private static CVurl cVurl = new CVurl();

    private static Account account;

    public static void setAccount(Account setAccount) {
        account = setAccount;
    }

    public static Account getAccount() {
        return account;
    }

    public static RequestBuilder requestBuilder(String url, String method, Map query) {
        RequestBuilder builder = null;

        if (method == "POST") {
            builder = cVurl.post(url);
        } else if (method == "GET") {
            builder = cVurl.get(url);
        }

        if (account != null) {
            if (account.token != "") {
                builder.header("Cookie", ".ROBLOSECURITY=" + account.token);
            }

            if (account.xcsrfToken != "") {
                builder.header("X-CSRF-TOKEN", account.xcsrfToken);
            }
        }

        if (query != null) {
            builder.queryParams(query);
        }

        return builder;
    }

    public static Response<String> sendRequest(String url, String method, Map query) throws RuntimeException {
        RequestBuilder builder = requestBuilder(url, method, query);

        var response = builder.asString();

        if (response.isPresent()) {
            return (Response<String>) response.get();
        } else {
            throw new RuntimeException("An error has occurred while processing your request!");
        }
    }

    public static ObjectNode sendRequestJSON(String url, String method, Map query) {
        RequestBuilder builder = requestBuilder(url, method, query);

        ObjectNode response = (ObjectNode) builder.asObject(ObjectNode.class);

        return response;
    }
}

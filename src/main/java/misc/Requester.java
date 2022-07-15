package misc;

import artifacts.account.Account;
import com.fasterxml.jackson.databind.node.ObjectNode;
import coresearch.cvurl.io.model.Response;
import coresearch.cvurl.io.request.CVurl;
import coresearch.cvurl.io.request.RequestBuilder;

import java.util.Map;
import java.util.Optional;

public class Requester {
    private CVurl cVurl = new CVurl();

    private Account account;

    public Requester(Account account) {
        this.account = account;
    }

    public Requester() {}

    public void setAccount(Account account) {
        this.account = account;
    }

    public RequestBuilder requestBuilder(String url, String method, String body, Map query) {
        RequestBuilder builder = null;

        if (method.equals("POST")) {
            builder = cVurl.post(url).body(body);
        } else if (method.equals("PATCH")) {
            builder = cVurl.patch(url).body(body);
        } else if (method.equals("DELETE")) {
            builder = cVurl.delete(url).body(body);
        } else if (method.equals("GET")) {
            builder = cVurl.get(url);
        }

        builder.header("Content-Type", "application/json");

        if (account != null) {
            if (!account.token.equals("")) {
                builder.header("Cookie", ".ROBLOSECURITY=" + account.token);
            }

            if (!account.xcsrfToken.equals("")) {
                builder.header("X-CSRF-TOKEN", account.xcsrfToken);
            }
        }

        if (query != null) {
            builder.queryParams(query);
        }

        return builder;
    }

    private Response<String> sendRequest(String url, String method, String body, Map query, int counter) throws RuntimeException {
        RequestBuilder builder = requestBuilder(url, method, body, query);

        Optional response = builder.asString();

        if (counter > 3) {
            throw new RuntimeException("Too many failed attempts at getting the X-CSRF-TOKEN. Maybe something is wrong with Roblox?");
        }

        if (response.isPresent()) {
            var result = (Response<String>) response.get();

            if (account != null) {
                Optional<String> xcsrfToken = result.headers().firstValue("X-CSRF-TOKEN");

                if (xcsrfToken.isPresent()) {
                    account.xcsrfToken = xcsrfToken.get();
                }
            }
            // Re-send the request with the now valid X-CSRF-TOKEN
            if (result.status() == 403) {
                return sendRequest(url, method, body, query, counter + 1);
            }

            return result;
        }

        throw new RuntimeException("An error has occurred while processing your request.");
    }

    public Response<String> sendRequest(String url, String method, String body, Map query) throws RuntimeException {
        return sendRequest(url, method, body, query, 0);
    }

    public Response<String> sendRequest(String url, String method, String body) throws RuntimeException {
        return sendRequest(url, method, body, null, 0);
    }

    public Response<String> sendRequest(String url, String method) throws RuntimeException {
        return sendRequest(url, method, null, null, 0);
    }

    public ObjectNode sendRequestJson(String url, String method, String body) {
        RequestBuilder builder = requestBuilder(url, method, body, null);

        ObjectNode result = (ObjectNode) builder.asObject(ObjectNode.class);

        return result;
    }
}

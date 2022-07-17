package misc;

import artifacts.account.Account;
import artifacts.exceptions.RequestException;
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

    public Account getAccount() { return this.account; }

    public RequestBuilder requestBuilder(String url, String method, String body, Map query) {
        RequestBuilder builder = null;

        // Set the method and body (for requests other than GET)
        if (method.equals("POST")) {
            builder = cVurl.post(url).body(body);
        } else if (method.equals("PATCH")) {
            builder = cVurl.patch(url).body(body);
        } else if (method.equals("DELETE")) {
            builder = cVurl.delete(url).body(body);
        } else if (method.equals("GET")) {
            builder = cVurl.get(url);
        }

        // JSON header
        builder.header("Content-Type", "application/json");

        // Check if there is a valid account
        if (this.account != null) {
            // Set the cookie
            if (!this.account.cookie.equals("")) {
                builder.header("Cookie", ".ROBLOSECURITY=" + this.account.cookie);
            }

            // Set the token
            if (!this.account.token.equals("")) {
                builder.header("X-CSRF-TOKEN", this.account.token);
            }
        }

        if (query != null) {
            // Set parameters
            builder.queryParams(query);
        }

        return builder;
    }

    private Response<String> sendRequest(String url, String method, String body, Map query, int counter) throws RequestException {
        RequestBuilder builder = requestBuilder(url, method, body, query);

        Optional response = builder.asString();

        if (counter > 3) {
            throw new RequestException("Too many failed attempts at getting the X-CSRF-TOKEN. Maybe something is wrong with Roblox?");
        }

        if (response.isPresent()) {
            var result = (Response<String>) response.get();

            if (this.account != null) {
                // Set the X-CSRF-TOKEN
                Optional<String> token = result.headers().firstValue("X-CSRF-TOKEN");

                // Check if the token is present
                if (token.isPresent()) {
                    this.account.token = token.get();
                }
            }
            // Re-send the request with the now valid X-CSRF-TOKEN
            if (result.status() == 403) {
                return sendRequest(url, method, body, query, counter + 1);
            }

            return result;
        }

        throw new RequestException("An error has occurred while processing your request.");
    }

    public Response<String> sendRequest(String url, String method, String body, Map query) throws RequestException {
        return sendRequest(url, method, body, query, 0);
    }

    public Response<String> sendRequest(String url, String method, String body) throws RequestException {
        return sendRequest(url, method, body, null, 0);
    }

    public Response<String> sendRequest(String url, String method) throws RequestException {
        return sendRequest(url, method, null, null, 0);
    }

    // Get a JSON Object instead of a normal one (THIS NEEDS TO BE RE-WRITTEN).
    public ObjectNode sendRequestJson(String url, String method, String body) {
        RequestBuilder builder = requestBuilder(url, method, body, null);

        ObjectNode result = (ObjectNode) builder.asObject(ObjectNode.class);

        return result;
    }
}

package misc;

import artifacts.client.Client;
import artifacts.exceptions.RequestException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import coresearch.cvurl.io.model.Response;
import coresearch.cvurl.io.request.CVurl;
import coresearch.cvurl.io.request.RequestBuilder;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class Requester {
    private CVurl cVurl = new CVurl();
    private Client client;
    private static ArrayList<Client> clients = new ArrayList<>();

    public Requester(Client client) {
        this.setAccount(client);
    }

    public Requester() {}

    public void setAccount(Client client) {
        if (!clients.contains(client)) {
            clients.add(client);
        }

        this.client = client;
    }

    public Client getAccount() {
        return clients.size() == 1 ? clients.get(0) : this.client;
    }

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
        if (this.client != null) {
            // Set the cookie
            if (!this.client.cookie.equals("")) {
                builder.header("Cookie", ".ROBLOSECURITY=" + this.client.cookie);
            }

            // Set the token
            if (!this.client.token.equals("")) {
                builder.header("X-CSRF-TOKEN", this.client.token);
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

            if (this.client != null) {
                // Set the X-CSRF-TOKEN
                Optional<String> token = result.headers().firstValue("X-CSRF-TOKEN");

                // Check if the token is present
                if (token.isPresent()) {
                    this.client.token = token.get();
                }
            }
            // If the request returns a 403, re-send the request with the now valid X-CSRF-TOKEN
            return result.status() == 403 ? sendRequest(url, method, body, query, counter + 1) : result;
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

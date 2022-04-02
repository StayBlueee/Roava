package misc;

import artifacts.account.Account;
import com.fasterxml.jackson.databind.node.ObjectNode;
import coresearch.cvurl.io.model.Response;
import coresearch.cvurl.io.request.CVurl;
import coresearch.cvurl.io.request.RequestBuilder;


import java.util.Map;

public class Requester {
    private  CVurl cVurl = new CVurl();

    private  Account account;

    public  void setAccount(Account setAccount) {
        account = setAccount;
    }

    public  Account getAccount() {
        return account;
    }

    public  RequestBuilder requestBuilder(String url, String method, Map query) {
        RequestBuilder builder = null;

        if (method == "POST") {
            builder = cVurl.post(url);
        } else if (method == "GET") {
            builder = cVurl.get(url);
        }

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

    public  Response<String> sendRequest(String url, String method, Map query) throws RuntimeException {
        RequestBuilder builder = requestBuilder(url, method, query);

        var response = builder.asString();

        if (response.isPresent()) {
            var result = (Response<String>) response.get();

            if (account != null) {
                String xcsrfToken = result.headers().firstValue("X-CSRF-TOKEN").get();

                if (!xcsrfToken.equals("")) {
                    account.xcsrfToken = xcsrfToken;
                }
            }

            return result;
        } else {
            throw new RuntimeException("An error has occurred while processing your request.");
        }
    }

    public  ObjectNode sendRequestJSON(String url, String method, Map query) {
        RequestBuilder builder = requestBuilder(url, method, query);

        ObjectNode result = (ObjectNode) builder.asObject(ObjectNode.class);

        return result;
    }
}

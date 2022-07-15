package artifacts.groups;

import artifacts.account.Account;
import artifacts.user.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import coresearch.cvurl.io.model.Response;
import misc.Requester;

import java.util.List;

public class Group {
    private String name;
    private long groupId;
    private Requester requester = new Requester();

    public Group(long groupId) {
        ObjectNode object = requester.sendRequestJson(String.format("https://groups.roblox.com/v2/groups?groupIds=%d", groupId), "GET", "");

        if (object.get("data").isArray() && object.get("data").size() > 0) {
            this.groupId = groupId;
            this.name = object.get("data").get(0).get("name").asText();
        } else {
            throw new RuntimeException("The provided group ID is invalid!");
        }
    }

    public void setAccount(Account account) {
        this.requester.setAccount(account);
    }

    public void rankUser(User user, Long roleset) throws Exception {
        Response<String> t = requester.sendRequest(String.format("https://groups.roblox.com/v1/groups/%d/users/%d", this.groupId, user.getUserId()), "PATCH", String.format("{\"roleId\":" + roleset + "}"));
        if(!t.isSuccessful()) {
            throw new Exception(t.getBody());
        }
    }

    public String getName() {
        return this.name;
    }

    public long getId() {
        return this.groupId;
    }
}
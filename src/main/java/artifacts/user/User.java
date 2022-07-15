package artifacts.user;

import artifacts.groups.Group;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import misc.Requester;

import java.util.ArrayList;

public class User {
    private String username;
    private long userId;
    private Requester requester = new Requester();

    public User(long userId) {
        ObjectNode object = requester.sendRequestJson(String.format("https://users.roblox.com/v1/users/%d", userId), "GET", "");

        if (object.get("name").isTextual()) {
            this.username = object.get("name").asText();
            this.userId = userId;
        } else {
            throw new RuntimeException("The provided user ID is invalid!");
        }
    }

    public User(String username) {
        ObjectNode object = requester.sendRequestJson("https://users.roblox.com/v1/usernames/users", "POST", String.format("{\"usernames\": [\"%s\"]}", username));

        if (object.get("data").isArray() && object.get("data").size() > 0) {
            this.username = username;
            this.userId = object.get("data").get(0).get("id").asLong();
        } else {
            throw new RuntimeException("The provided username is invalid!");
        }
    }

    public String getUsername() {
        return this.username;
    }

    public long getUserId() {
        return this.userId;
    }

    public ArrayList<Group> getGroups() {
        ArrayList<Group> groups = new ArrayList<Group>();

        ObjectNode object = requester.sendRequestJson(String.format("https://groups.roblox.com/v2/users/%d/groups/roles", this.userId), "GET", "");

        if (object.get("data").isArray() && object.get("data").size() > 0) {
            for (JsonNode array : object.get("data")) {
                groups.add(new Group(array.get("group").get("id").asLong()));
            }
        }

        return groups;
    }
}
package artifacts.user;

import artifacts.exceptions.GroupException;
import artifacts.exceptions.UserException;
import artifacts.groups.Group;
import artifacts.groups.GroupRole;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import misc.Requester;

import java.util.ArrayList;

public class User {
    private String username;
    private int userId;
    private Requester requester = new Requester();

    public User(int userId) throws UserException {
        ObjectNode object = requester.sendRequestJson(String.format("https://users.roblox.com/v1/users/%d", userId), "GET", "");

        if (object.get("name").isTextual()) {
            this.username = object.get("name").asText();
            this.userId = userId;
        } else {
            throw new UserException("The provided user ID is invalid!");
        }
    }

    public User(String username) throws UserException {
        ObjectNode object = requester.sendRequestJson("https://users.roblox.com/v1/usernames/users", "POST", String.format("{\"usernames\": [\"%s\"]}", username));

        if (object.get("data").isArray() && object.get("data").size() > 0) {
            this.username = username;
            this.userId = object.get("data").get(0).get("id").asInt();
        } else {
            throw new UserException("The provided username is invalid!");
        }
    }

    public String getUsername() {
        return this.username;
    }

    public int getUserId() {
        return this.userId;
    }

    public ArrayList<Group> getGroups() throws GroupException {
        ArrayList<Group> groups = new ArrayList<>();

        ObjectNode object = requester.sendRequestJson(String.format("https://groups.roblox.com/v2/users/%d/groups/roles", this.userId), "GET", "");

        if (object.get("data").isArray() && object.get("data").size() > 0) {
            for (JsonNode array : object.get("data")) {
                groups.add(new Group(array.get("group").get("id").asInt()));
            }
        }

        return groups;
    }

    // 99.9% sure this wont work
    public GroupRole getRankInGroup(Group group) throws GroupException {
        ObjectNode object = requester.sendRequestJson(String.format("https://groups.roblox.com/v2/users/%d/groups/roles", group.getId()), "GET", "");

        if (object.get("data").isArray() && object.get("data").size() > 0) {
            for (JsonNode array : object.get("data")) {
                if (array.get("group").get("id").asInt() == group.getGroupRole(this.getUsername()).getRoleId()) {
                    if(object.get("role").isArray()) {
                        return new GroupRole(array.get("id").asInt(), array.get("rank").asInt(), array.get("name").asText());
                    }
                }
            }
            return null;
        }
        throw new GroupException("The provided user is not in the provided group!");
    }
    public GroupRole getRankInGroup(int groupId) throws GroupException {
        Group group = new Group(groupId);
        ObjectNode object = requester.sendRequestJson(String.format("https://groups.roblox.com/v2/users/%d/groups/roles", group.getId()), "GET", "");

        if (object.get("data").isArray() && object.get("data").size() > 0) {
            for (JsonNode array : object.get("data")) {
                if (array.get("group").get("id").asInt() == group.getGroupRole(this.getUsername()).getRoleId()) {
                    if(object.get("role").isArray()) {
                        return new GroupRole(array.get("id").asInt(), array.get("rank").asInt(), array.get("name").asText());
                    }
                }
            }
            return null;
        }
        throw new GroupException("The provided user is not in the provided group!");
    }

}
package artifacts.groups;

import artifacts.account.Account;
import artifacts.exceptions.GroupException;
import artifacts.exceptions.RequestException;
import artifacts.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import coresearch.cvurl.io.model.Response;
import misc.Requester;

import java.util.ArrayList;

public class Group {
    private String name;
    private int groupId;
    private Requester requester = new Requester();

    public Group(int groupId) throws GroupException {
        ObjectNode object = requester.sendRequestJson(String.format("https://groups.roblox.com/v2/groups?groupIds=%d", groupId), "GET", "");

        if (object.get("data").isArray() && object.get("data").size() > 0) {
            this.groupId = groupId;
            this.name = object.get("data").get(0).get("name").asText();
        } else {
            throw new GroupException("The provided group ID is invalid!");
        }
    }

    public void setAccount(Account account) {
        this.requester.setAccount(account);
    }

    public ArrayList<GroupRole> getGroupRoles() {
        ArrayList<GroupRole> groupRoles = new ArrayList<>();

        ObjectNode object = requester.sendRequestJson(String.format("https://groups.roblox.com/v1/groups/%d/roles", this.groupId), "GET", "");

        if (object.get("roles").isArray() && object.get("roles").size() > 0) {
            for (JsonNode array : object.get("roles")) {
                groupRoles.add(new GroupRole(array.get("id").asInt(), array.get("rank").asInt(), array.get("name").asText()));
            }
        }

        return groupRoles;
    }

    public GroupRole getGroupRole(String roleName) throws GroupException {
        ArrayList<GroupRole> groupRoles = this.getGroupRoles();

        for (GroupRole groupRole : groupRoles) {
            if (groupRole.getRoleName().equals(roleName)) {
                return groupRole;
            }
        }

        throw new GroupException("The provided role name does not exist!");
    }

    public GroupRole getGroupRole(int roleNumber) throws GroupException {
        // If the provided role number is greater than 255, we can assume that it is a role ID
        if (roleNumber > 255) {
            // Check the role ID's details
            ObjectNode object = requester.sendRequestJson(String.format("https://groups.roblox.com/v1/roles?ids=%d", roleNumber), "GET", "");

            // Check if it returned a valid response
            if (object.get("data").isArray() && object.get("data").size() > 0) {
                JsonNode node = object.get("data").get(0);

                // Check if the provided group role ID is linked to the same group
                if (node.get("groupId").asInt() == this.groupId) {
                    return new GroupRole(node.get("id").asInt(), node.get("rank").asInt(), node.get("name").asText());
                }

                throw new GroupException("The provided role ID is not a part of this group!");
            }

            throw new GroupException("The provided role ID does not exist!");
        }

        // Check the group's roles
        ArrayList<GroupRole> groupRoles = this.getGroupRoles();

        for (GroupRole groupRole : groupRoles) {
            // Check if the role rank is the same as the provided role number
            if (groupRole.getRoleRank() == roleNumber) {
                return groupRole;
            }
        }

        throw new GroupException("The provided role does not exist!");
    }

    public void rankUser(User user, String roleName) throws GroupException, RequestException {
        this.rankUser(user.getUserId(), this.getGroupRole(roleName).getRoleId());
    }

    public void rankUser(int userId, String roleName) throws GroupException, RequestException {
        this.rankUser(userId, this.getGroupRole(roleName).getRoleId());
    }

    public void rankUser(int userId, int roleSet) throws GroupException, RequestException {
        if (this.requester.getAccount() == null) {
            throw new GroupException("No account has been provided!");
        }

        // If a role rank was provided, get the role ID.
        if (roleSet <= 255) {
            roleSet = getGroupRole(roleSet).getRoleId();
        }

        Response<String> response = requester.sendRequest(String.format("https://groups.roblox.com/v1/groups/%d/users/%d", this.groupId, userId), "PATCH", String.format("{\"roleId\":%d}", roleSet));

        if (!response.isSuccessful()) {
            throw new GroupException("Could not rank the provided user! " + response.status() + ": " + response.getBody());
        }
    }

    public void rankUser(User user, int roleSet) throws GroupException, RequestException {
        this.rankUser(user.getUserId(), roleSet);
    }

    public void exileUser(int userId) throws GroupException, RequestException {
        if (this.requester.getAccount() == null) {
            throw new GroupException("No account has been provided!");
        }

        Response<String> response = requester.sendRequest(String.format("https://groups.roblox.com/v1/groups/%d/users/%d", this.groupId, userId), "DELETE");

        if (!response.isSuccessful()) {
            throw new GroupException("Could not exile the provided user!");
        }
    }

    public void exileUser(User user) throws Exception {
        this.exileUser(user.getUserId());
    }

    public String getName() {
        return this.name;
    }

    public long getId() {
        return this.groupId;
    }
}
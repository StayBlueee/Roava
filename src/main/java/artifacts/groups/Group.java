package artifacts.groups;

import artifacts.exceptions.GroupIdNotValid;
import artifacts.user.User;
import com.fasterxml.jackson.databind.node.ObjectNode;
import misc.Requester;

import java.util.List;

interface GroupInterface {
    String getName();
    String getDescription();
    String getId();
    User getOwner();
    int getMemberCount();
    String getCreated();

    List<GroupRoles> getRoles(User user);
}

public class Group implements GroupInterface {
    String groupId;
    Requester requester = new Requester();
    ObjectNode result;
    public Group(String groupId) throws GroupIdNotValid {
        this.groupId = groupId;
        try {
            result = requester.sendRequestJSON(String.format("https://groups.roblox.com/v2/groups?groupIds=%s", groupId), "GET", null);
        } catch (Exception ex) {
            throw new GroupIdNotValid("The GroupID is invalid!");
        }
    }

    @Override
    public String getName() {
        return result.get("name").asText();
    }

    @Override
    public String getDescription() {
        return result.get("description").asText();
    }

    @Override
    public String getId() {
        return result.get("id").asText();
    }

    @Override
    public User getOwner() {
        return null; // null for now
    }

    @Override
    public int getMemberCount() {
        return result.get("memberCount").asInt();
    }

    @Override
    public String getCreated() {
        return result.get("created").asText();
    }


    @Override
    public List<GroupRoles> getRoles(User user) {
        return null; // null for now
    }
}
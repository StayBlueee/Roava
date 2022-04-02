package artifacts.user;

import artifacts.exceptions.UserIdNotValid;
import artifacts.groups.Group;
import com.fasterxml.jackson.databind.node.ObjectNode;
import misc.Requester;

import java.util.List;

interface UserInterface {
    String getUsername();
    String getDisplayName();
    Boolean isBanned();
    String getDescription();
    String getCreated();
    List<Group> groups();
}

public class User implements UserInterface {
    public String userId;
    ObjectNode result;
    public User(String userId) throws UserIdNotValid {
        this.userId = userId;
        try {
            result = requester.sendRequestJSON(String.format("https://users.roblox.com/v1/users/%s", userId), "GET", null);
        } catch (Exception ex) {
            throw new UserIdNotValid("The UserID is invalid!");
        }
    }
    List<Group> groups;
    Requester requester = new Requester();
    @Override
    public String getUsername() {
        return result.get("name").asText();
    }

    @Override
    public String getDisplayName() {
        return result.get("displayName").asText();
    }

    @Override
    public Boolean isBanned() {
        return result.get("isBanned").asBoolean();
    }

    @Override
    public String getDescription() {
        return result.get("description").asText();
    }

    @Override
    public String getCreated() {
        return result.get("created").asText();
    }


    @Override
    public List<Group> groups() {
        return groups;
    }
}
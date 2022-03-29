package artifacts.user;

import artifacts.Group;

import java.util.List;

interface UserInterface {
    String getUsername();
    String getUserId();
    Long userIdLong();
    List<Group> groups();
}

public class User implements UserInterface {
    String username;
    String userId;
    Long userIdLong;
    List<Group> groups;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public Long userIdLong() {
        return userIdLong;
    }

    @Override
    public List<Group> groups() {
        return groups;
    }
}
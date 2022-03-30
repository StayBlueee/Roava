package artifacts.user;

import artifacts.account.Account;
import artifacts.groups.Group;

import java.util.List;

interface UserInterface {
    String getUsername(User user);
    String getUserId(User user);
    Long userIdLong(User user);
    List<Group> groups();
}

public class User implements UserInterface {
    Account account;
    String username;
    String userId;
    Long userIdLong;
    List<Group> groups;

    @Override
    public String getUsername(User user) {
        return user.getUsername(user);
    }

    @Override
    public String getUserId(User user) {
        return user.userId;
    }

    @Override
    public Long userIdLong(User user) {
        return Long.parseLong(user.getUserId(user));
    }

    @Override
    public List<Group> groups() {
        return groups;
    }
}
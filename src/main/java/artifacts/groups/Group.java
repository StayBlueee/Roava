package artifacts.groups;

import artifacts.user.User;

import java.util.List;

interface GroupInterface {
    String getName();
    String getId();
    Long getIdLong();
    List<GroupRoles> getRoles(User user);
}

public class Group implements GroupInterface {
    String name;
    String id;
    Long idLong;
    List<GroupRoles> roles;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Long getIdLong() {
        return idLong;
    }

    @Override
    public List<GroupRoles> getRoles(User user) {
        return roles;
    }
}
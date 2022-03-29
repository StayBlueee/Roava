package artifacts.groups;

import artifacts.user.User;

import java.util.List;

public interface Group {
    String getName();
    String getId();
    Long getIdLong();
    List<GroupRoles> getRole(User user);
}

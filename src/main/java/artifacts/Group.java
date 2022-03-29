package artifacts;

import artifacts.user.User;

import java.util.List;

public interface Group {
    String getName();
    List<GroupRoles> getRole(User user);
}

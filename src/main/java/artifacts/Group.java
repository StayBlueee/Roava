package artifacts;

import java.util.List;

public interface Group {
    String getName();
    List<GroupRoles> getRole(User user);
}

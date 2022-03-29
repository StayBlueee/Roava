package artifacts;

import java.util.List;

public interface Group {
    String getName();
    List<Group> getRole(User user);
}

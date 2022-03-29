package artifacts.groups;

interface GroupRolesInterface {
    String getRoleId();
    String getName();
    String getRank();
    Long getRoleIdLong();
}
public class GroupRoles implements GroupRolesInterface {
    String roleId;
    String name;
    String rank;
    Long roleIdLong;
    @Override
    public String getRoleId() {
        return roleId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRank() {
        return rank;
    }

    @Override
    public Long getRoleIdLong() {
        return roleIdLong;
    }
}
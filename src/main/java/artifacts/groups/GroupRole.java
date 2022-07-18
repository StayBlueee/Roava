package artifacts.groups;

public class GroupRole {
    private int roleId;
    private int roleRank;
    private String roleName;

    public GroupRole(int roleId, int roleRank, String roleName) {
        this.roleId = roleId;
        this.roleRank = roleRank;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return this.roleId;
    }

    public int getRoleRank() {
        return this.roleRank;
    }

    public String getRoleName() {
        return this.roleName;
    }
}

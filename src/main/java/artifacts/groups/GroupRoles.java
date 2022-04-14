package artifacts.groups;

import artifacts.exceptions.GroupIdNotValid;

interface GroupRolesInterface {
    String getRoleId();
    String getName();
    String getRank();
    Long getRoleIdLong();
}
public class GroupRoles implements GroupRolesInterface {
    String groupId;
    public GroupRoles(String groupId) throws GroupIdNotValid {
        this.groupId = groupId;

        try {
            // request
        } catch (Exception ex) {
            throw new GroupIdNotValid("The GroupID is not valid.");
        }

    }


    @Override
    public String getRoleId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getRank() {
        return null;
    }

    @Override
    public Long getRoleIdLong() {
        return null;
    }
}
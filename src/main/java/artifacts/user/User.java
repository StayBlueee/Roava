package artifacts.user;

import artifacts.account.Account;
import artifacts.groups.Group;
import misc.Requester;
import okhttp3.RequestBody;
import okio.ByteString;

import java.util.List;

interface UserInterface {
    String getUsername(String userId);
    String getUserId(User user);
    Long userIdLong(User user);
    List<Group> groups();
}
public class User implements UserInterface {
    Account account = new Account("_|WARNING:-DO-NOT-SHARE-THIS.--Sharing-this-will-allow-someone-to-log-in-as-you-and-to-steal-your-ROBUX-and-items.|_622534382C9248FDCB67405A15E61C9CF7DA5C3F8D7B514462FFF1FBE4A6F162763E32032872C28995902F6E12B6E94872CCC7F1AF8FFE2B39EDF69F52B7BF484937D23EE34656762150DC8BC395746093B27CCF42E8862CEDF1DAB723302A4EF9D179236BAF000922C69116216AC1F435AE857F77553B091F65492F2ECE84C4F05F6B9244FEEF2359F20A815E254E47747DD6581D9BEF34A83CDEF79E3E9908DB8559E7EA1C0086D070FF51D82F6FA14D7F385626CE79BAB95CD5D9AC2661C73F633FA31ECF186D0F1C607314DB93B00A855C170C7EB553F6BA5DA2B33C66577D8326FA5557834E1C816C944740E21F7D7D30164ECE54D616381EAA0291D55554B01E8E2A22B5D77E6CEB8AE629617BD0BCE2F8400DA86065162E43419C42DABB71264CF519EAAD561DD32F9A56F4D8207FF05C9339AC91867F19ED1E3F1552BB40D8BE");
    Requester requester = new Requester();
    String username;
    String userId;
    Long userIdLong;
    List<Group> groups;



    @Override
    public String getUsername(String userId) {
        return requester.sendRequest(String.format("https://users.roblox.com/v1/users/%s", userId), "GET", null, account);
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
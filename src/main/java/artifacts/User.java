package artifacts;

public interface UserInterface {
    String username();
    String userId();
    Long userIdLong();
}

public class User implements UserInterface {
    String username;
    String userId;
    Long userIdLong;
}
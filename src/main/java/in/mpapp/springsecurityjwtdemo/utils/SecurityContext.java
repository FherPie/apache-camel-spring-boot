package in.mpapp.springsecurityjwtdemo.utils;

public class SecurityContext {

    private static final ThreadLocal<Long> CURRENT_USER = new ThreadLocal<>();

    static {
        CURRENT_USER.set(-999L);
    }

    public static void setCurrentUser(final Long currentUser) {
        CURRENT_USER.set(currentUser);
    }

    public static Long getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static void clearAll() {
        CURRENT_USER.remove();
    }
}

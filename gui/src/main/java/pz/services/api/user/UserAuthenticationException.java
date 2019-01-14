package pz.services.api.user;

public class UserAuthenticationException extends Exception {
    public String reason;

    public UserAuthenticationException(Throwable e) {
        reason = e.getMessage();
    }

    public UserAuthenticationException(String message) {
        super(message);
        reason = message;
    }
}

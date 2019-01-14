package pz.utils;

public class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
    public ServerException(String message, Throwable e) {
        super(message, e);
    }
}

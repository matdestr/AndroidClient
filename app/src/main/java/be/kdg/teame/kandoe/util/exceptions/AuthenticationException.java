package be.kdg.teame.kandoe.util.exceptions;

public class AuthenticationException extends Exception {

    public AuthenticationException(String detailMessage) {
        super(detailMessage);
    }

    public AuthenticationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}

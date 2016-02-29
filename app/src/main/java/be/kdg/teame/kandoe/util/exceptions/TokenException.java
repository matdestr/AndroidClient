package be.kdg.teame.kandoe.util.exceptions;

public class TokenException extends Exception {

    public TokenException(String detailMessage) {
        super(detailMessage);
    }

    public TokenException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}

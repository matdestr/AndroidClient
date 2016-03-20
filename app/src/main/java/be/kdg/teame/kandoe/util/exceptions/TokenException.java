package be.kdg.teame.kandoe.util.exceptions;

import be.kdg.teame.kandoe.data.retrofit.AccessToken;

/**
 * Contains an exception involving an {@link AccessToken}
 */
public class TokenException extends Exception {

    public TokenException(String detailMessage) {
        super(detailMessage);
    }

    public TokenException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}

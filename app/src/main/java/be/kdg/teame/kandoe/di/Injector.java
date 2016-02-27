package be.kdg.teame.kandoe.di;

import be.kdg.teame.kandoe.authentication.signin.SignInActivity;
import lombok.Getter;

public final class Injector {
    @Getter
    private static Class unauthenticatedRedirectActivity = SignInActivity.class;

    @Getter
    private static String clientDetailsHeader = "Basic YW5kcm9pZDpzZWNyZXQ=";

/*    @Getter
    private static String refreshTokenTemplate = "grant_type=refresh_token%n"*/
}

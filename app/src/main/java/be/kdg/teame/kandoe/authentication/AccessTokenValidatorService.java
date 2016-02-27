package be.kdg.teame.kandoe.authentication;

import android.app.IntentService;
import android.content.Intent;

public class AccessTokenValidatorService extends IntentService {
    public AccessTokenValidatorService() {
        super(AccessTokenValidatorService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}

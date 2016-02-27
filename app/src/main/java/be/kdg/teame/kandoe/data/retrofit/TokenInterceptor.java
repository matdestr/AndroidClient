package be.kdg.teame.kandoe.data.retrofit;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;

import be.kdg.teame.kandoe.di.Injector;
import be.kdg.teame.kandoe.util.http.HttpStatus;
import be.kdg.teame.kandoe.util.preferences.PrefManager;

public class TokenInterceptor implements Interceptor {
    private PrefManager prefManager;

    @Inject
    public TokenInterceptor(PrefManager prefManager) {
        this.prefManager = prefManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);

        if (response.code() == HttpStatus.UNAUTHORIZED){

            Request tokenRequest = new Request.Builder()
                    .url(ServiceGenerator.API_BASE_URL)
                    .addHeader("Authorization", Injector.getClientDetailsHeader())
                    .post(RequestBody.create(MediaType.parse("x-www-form-urlencoded"), ""))
                    .build();
        }

        return response;

    }
}

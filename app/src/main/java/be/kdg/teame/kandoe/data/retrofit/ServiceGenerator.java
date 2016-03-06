package be.kdg.teame.kandoe.data.retrofit;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import be.kdg.teame.kandoe.di.Injector;
import be.kdg.teame.kandoe.util.preferences.PrefManager;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Serves as a factory for Retrofit services.
 */
public class ServiceGenerator {
    private static final String API_BASE_URL = Injector.getApiBaseUrl();

    private static RestAdapter.Builder builder = new RestAdapter.Builder().setEndpoint(API_BASE_URL);


    /**
     * Shortens the timeout
     *
     * @return an httpClient which can be used to shorten the timeout
     */
    private static OkHttpClient getOkHttpClient() {
        OkHttpClient httpClient;
        httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        httpClient.setReadTimeout(5, TimeUnit.SECONDS);

        return httpClient;
    }

    /**
     * Creates a service to connect to the api
     *
     * @param serviceClass the service which is used
     * @param <T>          the class of the service
     * @return a service to the API
     */
    public static <T> T createService(Class<T> serviceClass) {
        builder.setClient(new OkClient(getOkHttpClient()));

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        });

        RestAdapter adapter = builder
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("RETROFIT"))
                .build();

        return adapter.create(serviceClass);
    }

    /**
     * Creates a service to connect to the api. extended with {@code Gson}
     *
     * @param serviceClass the service which is used
     * @param gson         to get the dates properly
     * @param <T>          the class of the service
     * @return a service to the API
     * @see Gson
     */
    public static <T> T createService(Class<T> serviceClass, Gson gson) {
        builder.setClient(new OkClient(getOkHttpClient()));

        builder.setConverter(new GsonConverter(gson));

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        });

        RestAdapter adapter = builder
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("RETROFIT"))
                .build();

        return adapter.create(serviceClass);
    }

    public static <T> T createSignInService(Class<T> serviceClass) {
        builder.setClient(new OkClient(getOkHttpClient()));

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", Injector.getClientDetailsHeader());
            }
        });

        RestAdapter adapter = builder
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("RETROFIT"))
                .build();

        return adapter.create(serviceClass);
    }

    /**
     * Creates a service to connect to the api provided with authorization
     *
     * @param serviceClass the service which is used
     * @param <T>          the class of the service
     * @return a service to the API
     */
    public static <T> T createAuthenticatedService(Class<T> serviceClass, PrefManager prefManager) {
        OkHttpClient httpClient = getOkHttpClient();
        httpClient.setAuthenticator(new TokenAuthenticator(prefManager));

        builder.setClient(new OkClient(httpClient));

        final AccessToken accessToken = prefManager.retrieveAccessToken();

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
                request.addHeader("Authorization", accessToken.getAuthorizationHeader());
            }
        });

        RestAdapter adapter = builder
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return adapter.create(serviceClass);
    }
}

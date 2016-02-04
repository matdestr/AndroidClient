package be.kdg.teame.kandoe.data.retrofit;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class ServiceGenerator {
    private static RestAdapter.Builder builder = new RestAdapter.Builder();

    /**
     * Shortens the timeout
     *
     * @return an httpClient which can be used to shorten the timeout
     */
    private static OkHttpClient getHttpClient() {
        OkHttpClient httpClient;
        httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        httpClient.setReadTimeout(10, TimeUnit.SECONDS);

        return httpClient;
    }

    /**
     * Creates a service to connect to the api
     *
     * @param serviceClass the service which is used
     * @param baseUrl      the base url of your server
     * @param <T>          the class of the service
     * @return a service to the API
     */
    public static <T> T createService(Class<T> serviceClass, String baseUrl) {
        builder.setEndpoint(baseUrl);

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        });

        RestAdapter adapter = builder
                .setClient(new OkClient(getHttpClient()))
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setLog(new AndroidLog("RETROFIT"))
                .build();

        return adapter.create(serviceClass);
    }

    /**
     * Creates a service to connect to the api. extended with {@code Gson}
     *
     * @param serviceClass the service which is used
     * @param baseUrl      the base url of your server
     * @param gson         to get the dates properly
     * @param <T>          the class of the service
     * @return a service to the API
     * @see Gson
     */
    public static <T> T createService(Class<T> serviceClass, String baseUrl, Gson gson) {
        builder.setEndpoint(baseUrl);
        builder.setConverter(new GsonConverter(gson));


        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        });

        RestAdapter adapter = builder
                .setClient(new OkClient(getHttpClient()))
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setLog(new AndroidLog("RETROFIT"))
                .build();

        return adapter.create(serviceClass);
    }
}

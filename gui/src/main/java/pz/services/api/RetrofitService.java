package pz.services.api;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.util.Strings;
import pz.services.settings.properties.PropertiesService;
import pz.services.settings.properties.Property;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class RetrofitService {
    private static RetrofitService instance;
    private static PropertiesService propertiesService;
    private static Retrofit retrofit;

    private RetrofitService() {
        propertiesService = PropertiesService.getInstance();
    }

    public static synchronized RetrofitService getInstance() {
        if (instance == null) {
            instance = new RetrofitService();
        }
        return instance;
    }

    public synchronized Retrofit getRetrofit() {
        String serverAddress = propertiesService.get(Property.SERVER_ADDRESS);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(serverAddress)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(createClient())
                    .build();
        }
        return retrofit;
    }

    private OkHttpClient createClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(chain -> {
            Request request = chain.request();
            log.debug("API request: {}", Optional.of(request).map(Request::toString).orElse(Strings.EMPTY));
            Response response = chain.proceed(request);
            log.debug("API response: {}", Optional.of(response).map(Response::toString).orElse(Strings.EMPTY));
            if (response.code() != 200) {
                throw new ServerApiException(response.message());
            }
            return response;
        });

        return clientBuilder.build();
    }

}

package mx.app.fashionme.restApi.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.RestApiConstants;
import mx.app.fashionme.restApi.deserializer.UserLoginDeserializer;
import mx.app.fashionme.restApi.model.UserResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by heriberto on 13/03/18.
 */

public class RestApiAdapter {

    private final OkHttpClient client;

    public RestApiAdapter() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    /**
     * Establece la conexion con el servidor restAPI
     */
    public EndpointsApi establecerConexionRestApi(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiConstants.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(EndpointsApi.class);
    }

    public EndpointsApi establecerConexionRestApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiConstants.ROOT_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(EndpointsApi.class);
    }

    public EndpointsApi establecerConexionOpenWeatherApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiConstants.ROOT_URL_OPENWEATHER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(EndpointsApi.class);
    }

    public EndpointsApi establecerConexionBodyApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiConstants.ROOT_URL_BODY_API_V2)
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(EndpointsApi.class);
    }

//    public Gson construyeGsonDeserializadorCategories() {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(CategoryResponse.class, new CategoryDeserializer());
//        return  gsonBuilder.create();
//    }

    public Gson construyeGsonDeserializadorUserLogin() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserResponse.class, new UserLoginDeserializer());
        return gsonBuilder.create();
    }

    public EndpointsApi establecerConexionRestApiLocal() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiConstants.ROOT_URL_LOCAL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(EndpointsApi.class);
    }
}

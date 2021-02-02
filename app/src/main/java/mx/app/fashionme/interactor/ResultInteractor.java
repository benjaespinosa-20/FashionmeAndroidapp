package mx.app.fashionme.interactor;

import android.content.Context;

import java.io.IOException;
import java.util.StringTokenizer;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IResultInteractor;
import mx.app.fashionme.pojo.APIBody;
import mx.app.fashionme.pojo.BodyType;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultInteractor implements IResultInteractor {

    public static final String TAG = ResultInteractor.class.getSimpleName();

    @Override
    public void getResults(Context context, OnGetResultListener callback) {
        String userName         = SessionPrefs.get(context).getUserName();
        String bodyName         = SessionPrefs.get(context).getBodyType(context.getString(R.string.app_language));
        String colorName        = SessionPrefs.get(context).getColor(context.getString(R.string.app_language));
        String colorImageUrl    = SessionPrefs.get(context).getColorImage();

        if (bodyName == null){
            callback.onFail("body");
            return;
        }

        if (colorName== null) {
            callback.onFail("color");
            return;
        }

        callback.onSuccess(toCapitalName(userName), bodyName, toCapitalName(colorName), colorImageUrl);
    }

    @Override
    public void sendTokenAPIBody(String token, Context context, OnGetResultListener callback) {
        // finally, execute the request
        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionBodyApi();

        Call<APIBody> apiBodyCall = endpointsApi.getRespuestaAPIv2(token);

        apiBodyCall.enqueue(new Callback<APIBody>() {
            @Override
            public void onResponse(Call<APIBody> call, Response<APIBody> response) {
                if (!response.isSuccessful()) {
                    callback.onFail("Algo salió mal, intenta más tarde");
                    return;
                }
                if (response.body() != null) {
                    sendToFashion(response.body().getCuerpo(), context, callback);
                }
            }

            @Override
            public void onFailure(Call<APIBody> call, Throwable t) {
                t.printStackTrace();
                callback.onFail("Red no disponible.");
            }
        });

    }

    private void sendToFashion(String cuerpo, Context context, OnGetResultListener callback) {

        int userId = SessionPrefs.get(context).getUserId();

        BodyType bodyType = new BodyType();

        switch (cuerpo) {
            case "oval":
                bodyType.setName("manzana");
                break;
            case "triangulo":
                bodyType.setName("triangulo");
                break;
            case "columna":
                bodyType.setName("columna");
                break;
            case "reloj arena":
                bodyType.setName("reloj arena");
                break;
            case "triangulo invertido":
                bodyType.setName("triangulo_invertido");
                break;
            case "trapecio":
                bodyType.setName("trapecio");
                break;
            default:
                bodyType.setName("columna");
                break;
        }

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<Base<User>> bodyResponse = endpointsApi.registerBody(bodyType, userId);

        bodyResponse.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")){
                        try {
                            SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.UNSENT_PHOTO);
                            callback.onFail(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.UNSENT_PHOTO);
                            e.printStackTrace();
                            callback.onFail("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.UNSENT_PHOTO);
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onFail(apiError != null ? apiError.getError() : "Algo salió mal");
                        return;
                    }
                }

                SessionPrefs.get(context).saveBody(
                        response.body().getData().getBody().getData().getSpanish().getBody_type(),
                        response.body().getData().getBody().getData().getEnglish().getBody_type(),
                        response.body().getData().getBody().getData().getUrlImage());
                SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.SENT_PHOTO);


                String userName         = SessionPrefs.get(context).getUserName();
                String bodyName         = SessionPrefs.get(context).getBodyType(context.getString(R.string.app_language));
                String colorName        = SessionPrefs.get(context).getColor(context.getString(R.string.app_language));
                String colorImageUrl    = SessionPrefs.get(context).getColorImage();

                callback.onSuccess(userName, bodyName, colorName, colorImageUrl);

            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                t.printStackTrace();
                SessionPrefs.get(context).saveBody(null, null, null);
                callback.onFail("Algo salió mal, intenta más tarde.");
            }
        });

    }

    private String toCapitalName(String name) {
        if (name == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        StringTokenizer st = new StringTokenizer(name, " ");
        while (st.hasMoreElements()) {
            String ne = (String)st.nextElement();
            if (ne.length() > 0) {
                builder.append(ne.substring(0, 1).toUpperCase());
                builder.append(ne.substring(1).toLowerCase());
                builder.append(' ');
            }
        }
        return builder.toString();
    }
}

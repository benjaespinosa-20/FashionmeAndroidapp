package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mx.app.fashionme.interactor.interfaces.IMyClosetInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyClosetInteractor implements IMyClosetInteractor {

    private Context context;

    public MyClosetInteractor(Context context) {
        this.context = context;
    }

    @Override
    public void getClothesByUser(int userId, MyClosetListener listener) {

        RestApiAdapter adapter = new RestApiAdapter();
        EndpointsApi endpointsApi = adapter.establecerConexionRestApi();

        Call<Base<ArrayList<Clothe>>> call = endpointsApi.getClothesByUser(userId);

        call.enqueue(new Callback<Base<ArrayList<Clothe>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Clothe>>> call, Response<Base<ArrayList<Clothe>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        listener.onErrorGetData("Error en el servidor, intenta mas tarde");
                        return;
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onErrorGetData(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }

                List<Clothe> data = new ArrayList<>();

                if (response.body() != null) {
                    data.addAll(response.body().getData());
                }

                listener.updateData(data);
            }

            @Override
            public void onFailure(Call<Base<ArrayList<Clothe>>> call, Throwable t) {
                Log.e("MyCloset", "onFailure", t);
                listener.onErrorGetData("Error en la conexion, intenta mas tarde");
            }
        });
    }
}

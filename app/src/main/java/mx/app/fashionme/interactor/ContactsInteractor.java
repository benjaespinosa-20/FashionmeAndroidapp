package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IContactsInteractor;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsInteractor implements IContactsInteractor {

    private static final String TAG = "Contacts";
    @Override
    public void getData(Context context, ContactsListener listener) {

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<ArrayList<ChatAssessorClient>>> baseCall = endpointsApi.getChats(String.valueOf(SessionPrefs.get(context).getUserId()));
        baseCall.enqueue(new Callback<Base<ArrayList<ChatAssessorClient>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<ChatAssessorClient>>> call, Response<Base<ArrayList<ChatAssessorClient>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.onErrorGetData(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, "Error en la peticion", e.getCause());
                            e.printStackTrace();
                            listener.onErrorGetData("Algo salio mal, intenta mas tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onErrorGetData(apiError != null ? apiError.getError():"Algo salió mal, intenta más tarde");
                        return;
                    }
                }

                ArrayList<ChatAssessorClient> myChats = new ArrayList<>();
                String myEmail = SessionPrefs.get(context).getEmail();

                for (ChatAssessorClient chat:response.body().getData()) {
                    if (chat.getAssessor() != null){
                        if (chat.getAssessor().toLowerCase().equals(myEmail.toLowerCase())) {
                            myChats.add(chat);
                        }
                    }
                }

                listener.onGetData(myChats);
            }

            @Override
            public void onFailure(Call<Base<ArrayList<ChatAssessorClient>>> call, Throwable t) {
                Log.e(TAG, "Error en la conexion", t);
                t.printStackTrace();
                listener.onErrorGetData(t.getMessage());
            }
        });
    }
}

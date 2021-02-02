package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IChatsInteractor;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsInteractor implements IChatsInteractor {

    private static final String TAG = "Chats";

    @Override
    public void getData(Context context, ChatsListener listener) {
        RestApiAdapter apiAdapter = new RestApiAdapter();

        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<ArrayList<ChatAssessorClient>>> baseCall = endpointsApi.getChats(null);
        baseCall.enqueue(new Callback<Base<ArrayList<ChatAssessorClient>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<ChatAssessorClient>>> call, Response<Base<ArrayList<ChatAssessorClient>>> response) {
                if (!response.isSuccessful()){
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
                        ApiError error = ApiError.fromResponseBody(response.errorBody());
                        listener.onErrorGetData(error != null ? error.getError():"Algo salio mal, intenta mas tarde");
                        return;
                    }
                }

                ArrayList<ChatAssessorClient> pendingChats = new ArrayList<>();

                for (ChatAssessorClient assessorClient:response.body().getData()) {
                    if (assessorClient.getAssessor() == null){
                        pendingChats.add(assessorClient);
                    }
                }

                listener.onGetData(pendingChats);
            }

            @Override
            public void onFailure(Call<Base<ArrayList<ChatAssessorClient>>> call, Throwable t) {
                Log.e(TAG, "Error en la conexion", t);
                t.printStackTrace();
                listener.onErrorGetData(t.getMessage());
            }
        });
    }

    @Override
    public void updateChat(Context context, ChatAssessorClient chatAssessorClient, ChatsListener listener) {

        chatAssessorClient.setAssessor(SessionPrefs.get(context).getEmail());

        RestApiAdapter apiAdapter = new RestApiAdapter();

        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<ChatAssessorClient>> call = endpointsApi.updateChat(chatAssessorClient, chatAssessorClient.getId());

        call.enqueue(new Callback<Base<ChatAssessorClient>>() {
            @Override
            public void onResponse(Call<Base<ChatAssessorClient>> call, Response<Base<ChatAssessorClient>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")){
                        try {
                            listener.onErrorUpdateChat(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            listener.onErrorUpdateChat("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onErrorUpdateChat(apiError != null ? apiError.getError() : "Algo salió mal");
                        return;
                    }
                }

                listener.onSuccessUpdateChat(response.body().getData());

            }

            @Override
            public void onFailure(Call<Base<ChatAssessorClient>> call, Throwable t) {
                Log.e(TAG, "ERROR UPDATE CHAT", t);
                listener.onErrorUpdateChat(t.getMessage());
            }
        });
    }
}

package mx.app.fashionme.interactor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mx.app.fashionme.fragment.ExtraCalidoFragment;
import mx.app.fashionme.fragment.ExtraFrioFragment;
import mx.app.fashionme.interactor.interfaces.IExtraTestInteractor;
import mx.app.fashionme.pojo.Answer;
import mx.app.fashionme.pojo.QuestionColor;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import mx.app.fashionme.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtraTestInteractor implements IExtraTestInteractor {

    public static final String TAG = ExtraTestInteractor.class.getSimpleName();

    private List<QuestionColor> questions;

    @Override
    public void getQuestions(Context context, ExtraTestListener listener) {
        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<ArrayList<QuestionColor>>> call = endpointsApi.getQuestionsColor();

        call.enqueue(new Callback<Base<ArrayList<QuestionColor>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<QuestionColor>>> call, Response<Base<ArrayList<QuestionColor>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            Log.e(TAG, response.errorBody().string());
                            listener.onError(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            listener.onError("Algo salio mal, intenta mas tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        Log.e(TAG, apiError.getError());
                        listener.onError(apiError != null ? apiError.getError() : "");
                        return;
                    }
                }

                if (response.body() != null && response.body().getData().size() > 0) {


                    questions = new ArrayList<>();

                    for (QuestionColor question:response.body().getData()) {
                        if (!question.getType().equals("normal")) {
                            questions.add(question);
                        }

                    }

                    listener.updateData(questions);
                }

            }

            @Override
            public void onFailure(Call<Base<ArrayList<QuestionColor>>> call, Throwable t) {
                Log.d(TAG, "onFailure", t);
                listener.onError("Algo salio mal, intenta mas tarde");
            }
        });

    }

    @Override
    public void getFragment(Context context, String color, ExtraTestListener callback) {

        switch (color) {
            case "calido":
                ExtraCalidoFragment fragment = new ExtraCalidoFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) getQuestions());
                fragment.setArguments(bundle);
                callback.onSuccessColor(fragment, Utils.EXTRA_CALIDO_FRAGMENT);
                return;
            case "frio":
                ExtraFrioFragment frioFragment = new ExtraFrioFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("list", (Serializable) getQuestions());
                frioFragment.setArguments(bundle1);
                callback.onSuccessColor(frioFragment, Utils.EXTRA_FRIO_FRAGMENT);
                return;
        }

        ExtraCalidoFragment fragment = new ExtraCalidoFragment();
        callback.onSuccessColor(fragment, Utils.EXTRA_CALIDO_FRAGMENT);
    }

    @Override
    public void sendResultExtra(final Context context, String color, final ExtraTestListener callback) {
        if (color == null){
            callback.onError("Debes elegir una respuesta");
            return;
        }

        Answer answer = new Answer();

        switch (color) {
            case "spring":
                answer.setSpring();
                break;
            case "summer":
                answer.setSummer();
                break;
            case "fall":
                answer.setFall();
                break;
            case "winter":
                answer.setWinter();
                break;
        }

        int userId = SessionPrefs.get(context).getUserId();
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<Base<User>> userCall = endpointsApi.sendExtraTest(answer, userId);
        userCall.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            Log.e(TAG, response.errorBody().string());
                            callback.onError(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            callback.onError("Algo salio mal, intenta mas tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onError(apiError != null ? apiError.getError() : null);
                        return;
                    }
                }

                String colorSpanish = response.body().getData().getColor().getData().getSpanish().getColor_name().toLowerCase();
                String colorEnglish = response.body().getData().getColor().getData().getEnglish().getColor_name().toLowerCase();
                SessionPrefs.get(context).saveColor(colorSpanish, colorEnglish, response.body().getData().getColor().getData().getUrlImage());

                callback.onSuccessSendResult();
            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                t.printStackTrace();
                callback.onError("Algo salio mal, intenta mas tarde");
            }
        });
    }

    public List<QuestionColor> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionColor> questions) {
        this.questions = questions;
    }
}

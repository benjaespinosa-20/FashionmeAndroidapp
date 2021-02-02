package mx.app.fashionme.interactor;

import android.content.Context;

import androidx.viewpager.widget.ViewPager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IStyleTestInteractor;
import mx.app.fashionme.pojo.AnswerStyle;
import mx.app.fashionme.pojo.Question;
import mx.app.fashionme.pojo.QuestionStyle;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heriberto on 3/04/18.
 */

public class StyleTestInteractor implements IStyleTestInteractor {

    public static final String TAG = StyleTestInteractor.class.getSimpleName();
    private ArrayList<Question> questions;

    @Override
    public void getQuestions(Context context, final OnGetStyleQuestionListener callback) {
        String genre = getGenre(context);

        if (genre == null){
            callback.onError("Ocurrio un error al cargar las preguntas, intenta mas tarde");
            return;
        }

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        switch (genre){
            case "woman":
                Call<Base<ArrayList<QuestionStyle>>> questionsCall = endpointsApi.getQuestionsStyleWoman();
                questionsCall.enqueue(new Callback<Base<ArrayList<QuestionStyle>>>() {
                    @Override
                    public void onResponse(Call<Base<ArrayList<QuestionStyle>>> call, Response<Base<ArrayList<QuestionStyle>>> response) {
                        if (!response.isSuccessful()) {
                            if (response.errorBody().contentType().type().equals("text")) {
                                callback.onError("Algo salio mal, intenta más tarde");
                                try {
                                    Log.e(TAG,response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return;
                            } else {
                                ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                                callback.onError(apiError.getError());
                                Log.e(TAG, apiError.getError());
                                return;
                            }
                        }
                        callback.onSuccess(response.body().getData());
                    }

                    @Override
                    public void onFailure(Call<Base<ArrayList<QuestionStyle>>> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        t.printStackTrace();
                        callback.onError("Error en la conexión");
                    }
                });
                break;

            case  "man":
                Call<Base<ArrayList<QuestionStyle>>> questionsCallMan = endpointsApi.getQuestionsStyleMan();
                questionsCallMan.enqueue(new Callback<Base<ArrayList<QuestionStyle>>>() {
                    @Override
                    public void onResponse(Call<Base<ArrayList<QuestionStyle>>> call, Response<Base<ArrayList<QuestionStyle>>> response) {
                        if (!response.isSuccessful()) {
                            if (response.errorBody().contentType().type().equals("text")) {
                                callback.onError("Algo salio mal, intenta más tarde");
                                try {
                                    Log.e(TAG,response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return;
                            } else {
                                ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                                callback.onError(apiError.getError());
                                Log.e(TAG, apiError.getError());
                                return;
                            }
                        }
                        callback.onSuccess(response.body().getData());
                    }

                    @Override
                    public void onFailure(Call<Base<ArrayList<QuestionStyle>>> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                        t.printStackTrace();
                        callback.onError("Error en la conexión");
                    }
                });

                break;

            default:
                callback.onError("Ocurrio un error al cargar las preguntas de tu genero. Intenta mas tarde");
                break;
        }
    }

    @Override
    public void sendStyleAnswers(final Context context, AnswerStyle answerStyle, ViewPager viewPager, final OnSendAnswersFinishedListener callback) {

        int userId = SessionPrefs.get(context).getUserId();

        int total = answerStyle.getA()+answerStyle.getB()+answerStyle.getC()+
                answerStyle.getD()+answerStyle.getE()+answerStyle.getF()+ answerStyle.getG();

        if (total < viewPager.getAdapter().getCount()) {
            callback.onFailSend("Debes responder todas las preguntas");
            return;
        }

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        final Call<Base<User>> userCall  = endpointsApi.sendStyleTest(answerStyle, userId);
        userCall.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        callback.onFailSend("Algo salio mal, intenta más tarde");
                        try {
                            Log.e(TAG,response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onFailSend(apiError.getError());
                        Log.e(TAG, apiError.getError());
                        return;
                    }
                }

                String lang = context.getString(R.string.app_language);
                String style = null;

                switch (lang.toLowerCase()) {
                    case "spanish":
                        style = response.body().getData().getStyle().getData().getSpanish().getName();
                        break;
                    case "english":
                        style = response.body().getData().getStyle().getData().getEnglish().getName();
                        break;
                    default:
                        style = response.body().getData().getStyle().getData().getSpanish().getName();
                        break;
                }

                callback.onSuccessSend(style);
            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                callback.onFailSend("Error en la conexión");
            }
        });
    }

    @Override
    public String getGenre(Context context) {
        return SessionPrefs.get(context).getGenre();
    }

}

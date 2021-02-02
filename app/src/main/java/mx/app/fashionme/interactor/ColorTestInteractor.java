package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IColorTestInteractor;
import mx.app.fashionme.pojo.Answer;
import mx.app.fashionme.pojo.AnswerColor;
import mx.app.fashionme.pojo.ColorAnswerViewModel;
import mx.app.fashionme.pojo.ColorQuestionsViewModel;
import mx.app.fashionme.pojo.QuestionColor;
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
 * Created by heriberto on 22/03/18.
 */

public class ColorTestInteractor implements IColorTestInteractor {

    private String TAG = ColorTestInteractor.class.getSimpleName();

    @Override
    public void getQuestions(Context context, TestListener listener) {

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
                            listener.onErrorGet(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            listener.onErrorGet("Algo salio mal, intenta mas tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        Log.e(TAG, apiError.getError());
                        listener.onErrorGet(apiError != null ? apiError.getError() : "");
                        return;
                    }
                }

                if (response.body() != null && response.body().getData().size() > 0){
                    List<ColorQuestionsViewModel> data = new ArrayList<>();
                    for (QuestionColor question:response.body().getData()) {

                        if (question.getType().equals("normal")) {
                            if (context.getString(R.string.app_language).equals("spanish")) {

                                List<ColorAnswerViewModel> ans = new ArrayList<>();

                                for (AnswerColor answer:question.getAnswers().getData()) {
                                    ans.add(new ColorAnswerViewModel(
                                            answer.getSpanish().getAnswer(),
                                            answer.getIndent())
                                    );
                                }

                                ColorQuestionsViewModel questionsViewModel = new ColorQuestionsViewModel(
                                        question.getSpanish().getQuestion(),
                                        ans);


                                data.add(questionsViewModel);

                            } else if (context.getString(R.string.app_language).equals("english")) {
                                List<ColorAnswerViewModel> ans = new ArrayList<>();

                                for (AnswerColor answer:question.getAnswers().getData()) {
                                    ans.add(new ColorAnswerViewModel(
                                            answer.getEnglish().getAnswer(),
                                            answer.getIndent())
                                    );
                                }

                                ColorQuestionsViewModel questionsViewModel = new ColorQuestionsViewModel(
                                        question.getEnglish().getQuestion(),
                                        ans);


                                data.add(questionsViewModel);

                            }
                        }

                    }
                    listener.updateData(data);
                }

            }

            @Override
            public void onFailure(Call<Base<ArrayList<QuestionColor>>> call, Throwable t) {
                Log.d(TAG, "onFailure", t);
                listener.onErrorGet("Algo salio mal, intenta mas tarde");
            }
        });
    }

    @Override
    public void sendAnswersToServer(List<ColorQuestionsViewModel> resultList, Context context, TestListener listener) {

        Answer answer = new Answer();

        for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i).getUserAnswer() == null ){
                    listener.onErrorSend("Debes responder todas las preguntas");
                    return;
            }
        }

        answer.setQuestion_1(resultList.get(0).getUserAnswer());
        answer.setQuestion_2(resultList.get(1).getUserAnswer());
        answer.setQuestion_3(resultList.get(2).getUserAnswer());
        answer.setQuestion_4(resultList.get(3).getUserAnswer());
        answer.setQuestion_5(resultList.get(4).getUserAnswer());
        answer.setQuestion_6(resultList.get(5).getUserAnswer());
        answer.setQuestion_7(resultList.get(6).getUserAnswer());
        answer.setQuestion_8(resultList.get(7).getUserAnswer());
        answer.setQuestion_9(resultList.get(8).getUserAnswer());

        // Send colors
        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<Base<User>> responseCall = endpointsApi.sendTest(answer, userId);
        responseCall.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            Log.e(TAG, response.errorBody().string());
                            listener.onErrorSend(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            listener.onErrorSend("Algo salio mal, intenta mas tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        Log.e(TAG, apiError.getError());
                        listener.onErrorSend(apiError != null ? apiError.getError() : "Algo salio mal intenta mas tarde");
                        return;
                    }
                }

                if (response.body() != null) {

                    if (response.body().getData().getColor() == null) {
                        listener.onSuccessSend(true);
                    } else {
                        SessionPrefs.get(context).saveColor(
                                response.body().getData().getColor().getData().getSpanish().getColor_name(),
                                response.body().getData().getColor().getData().getEnglish().getColor_name(),
                                response.body().getData().getColor().getData().getUrlImage()
                        );
                        listener.onSuccessSend(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                Log.d(TAG, "onFailure", t);
                listener.onErrorSend("Algo salio mal, intenta mas tarde");
            }
        });
    }
}

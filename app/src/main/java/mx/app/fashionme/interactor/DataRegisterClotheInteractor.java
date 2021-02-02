package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IDataRegisterClotheInteractor;
import mx.app.fashionme.pojo.Climate;
import mx.app.fashionme.pojo.Colorimetry;
import mx.app.fashionme.pojo.DataRegisterClotheViewModel;
import mx.app.fashionme.pojo.DressCode;
import mx.app.fashionme.pojo.Subcategory;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRegisterClotheInteractor implements IDataRegisterClotheInteractor {

    private Context context;

    @Override
    public void getData(Context context, String type, DataListener listener) {

        this.context = context;

        if (type == null) {
            listener.onErrorGetData("Ocurrio un error intentalo nuevamente");
            return;
        }

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        switch (type) {

            case "dress-code":
                Call<Base<ArrayList<DressCode>>> call = endpointsApi.getDressCodes();
                callToDressCode(call, listener);
                break;
            case "climate":
                Call<Base<ArrayList<Climate>>> callClimate = endpointsApi.getClimates();
                callToClimate(callClimate, listener);
                break;
            case "subcategories":
                Call<Base<ArrayList<Subcategory>>> callSubcategory = endpointsApi.getSubcategoriesAll();
                callToSubcategory(callSubcategory, listener);
                break;
            case "colors":
                Call<Base<ArrayList<Colorimetry>>> callColor = endpointsApi.getColors();
                callToColor(callColor, listener);
                break;
        }

    }

    private void callToDressCode(Call<Base<ArrayList<DressCode>>> call, DataListener listener) {

        call.enqueue(new Callback<Base<ArrayList<DressCode>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<DressCode>>> call, Response<Base<ArrayList<DressCode>>> response) {
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

                String genre = SessionPrefs.get(context).getGenre();

                if (response.body() != null) {

                    List<DataRegisterClotheViewModel> data = new ArrayList<>();

                    for (DressCode dressCode:response.body().getData()) {


                        if (dressCode.getGenre().equals(genre)) {
                            if (context.getString(R.string.app_language).equals("spanish")) {

                                DataRegisterClotheViewModel viewModel = new DataRegisterClotheViewModel(
                                        dressCode.getId(),
                                        dressCode.getSpanish().getName()
                                );

                                data.add(viewModel);

                            } else if (context.getString(R.string.app_language).equals("english")) {

                                DataRegisterClotheViewModel viewModelEng = new DataRegisterClotheViewModel(
                                        dressCode.getId(),
                                        dressCode.getEnglish().getName()
                                );

                                data.add(viewModelEng);
                            }
                        }
                    }

                    listener.updateData(data);
                }
            }

            @Override
            public void onFailure(Call<Base<ArrayList<DressCode>>> call, Throwable t) {
                Log.e("DataRegisterData", "onFailure", t);
                listener.onErrorGetData("Algo salio mal, intenta mas tarde");
            }
        });
    }

    private void callToClimate(Call<Base<ArrayList<Climate>>> call, DataListener listener) {

        call.enqueue(new Callback<Base<ArrayList<Climate>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Climate>>> call, Response<Base<ArrayList<Climate>>> response) {
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

                if (response.body() != null) {

                    List<DataRegisterClotheViewModel> data = new ArrayList<>();

                    for (Climate climate:response.body().getData()) {

                        if (context.getString(R.string.app_language).equals("spanish")) {

                            DataRegisterClotheViewModel viewModel = new DataRegisterClotheViewModel(
                                    climate.getId(),
                                    climate.getSpanish().getName()
                            );

                            data.add(viewModel);

                        } else if (context.getString(R.string.app_language).equals("english")) {

                            DataRegisterClotheViewModel viewModelEng = new DataRegisterClotheViewModel(
                                    climate.getId(),
                                    climate.getEnglish().getName()
                            );

                            data.add(viewModelEng);
                        }

                    }

                    listener.updateData(data);
                }
            }

            @Override
            public void onFailure(Call<Base<ArrayList<Climate>>> call, Throwable t) {
                Log.e("DataRegisterData", "onFailure", t);
                listener.onErrorGetData("Algo salio mal, intenta mas tarde");
            }

        });
    }

    private void callToSubcategory(Call<Base<ArrayList<Subcategory>>> call, DataListener listener) {

        call.enqueue(new Callback<Base<ArrayList<Subcategory>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Subcategory>>> call, Response<Base<ArrayList<Subcategory>>> response) {
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

                String genre = SessionPrefs.get(context).getGenre().equals("man") ? "m":"w";

                if (response.body() != null) {

                    List<DataRegisterClotheViewModel> data = new ArrayList<>();

                    for (Subcategory subcategory:response.body().getData()) {

                        if (subcategory.getGenre().equals(genre) || subcategory.getGenre().equals("b")) {
                            if (context.getString(R.string.app_language).equals("spanish")) {

                                DataRegisterClotheViewModel viewModel = new DataRegisterClotheViewModel(
                                        subcategory.getId(),
                                        subcategory.getSpanish().getName()
                                );

                                data.add(viewModel);

                            } else if (context.getString(R.string.app_language).equals("english")) {

                                DataRegisterClotheViewModel viewModelEng = new DataRegisterClotheViewModel(
                                        subcategory.getId(),
                                        subcategory.getEnglish().getName()
                                );

                                data.add(viewModelEng);
                            }
                        }
                    }

                    listener.updateData(data);
                }
            }

            @Override
            public void onFailure(Call<Base<ArrayList<Subcategory>>> call, Throwable t) {
                Log.e("DataRegisterData", "onFailure", t);
                listener.onErrorGetData("Algo salio mal, intenta mas tarde");
            }
        });
    }

    private void callToColor(Call<Base<ArrayList<Colorimetry>>> call, DataListener listener) {

        call.enqueue(new Callback<Base<ArrayList<Colorimetry>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Colorimetry>>> call, Response<Base<ArrayList<Colorimetry>>> response) {
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

                if (response.body() != null) {

                    List<DataRegisterClotheViewModel> data = new ArrayList<>();

                    for (Colorimetry color:response.body().getData()) {

                        if (context.getString(R.string.app_language).equals("spanish")) {

                            DataRegisterClotheViewModel viewModel = new DataRegisterClotheViewModel(
                                    color.getId(),
                                    color.getSpanish().getColor_name()
                            );

                            data.add(viewModel);

                        } else if (context.getString(R.string.app_language).equals("english")) {

                            DataRegisterClotheViewModel viewModelEng = new DataRegisterClotheViewModel(
                                    color.getId(),
                                    color.getEnglish().getColor_name()
                            );

                            data.add(viewModelEng);
                        }

                    }

                    listener.updateData(data);
                }
            }

            @Override
            public void onFailure(Call<Base<ArrayList<Colorimetry>>> call, Throwable t) {
                Log.e("DataRegisterData", "onFailure", t);
                listener.onErrorGetData("Algo salio mal, intenta mas tarde");
            }

        });
    }
}

package mx.app.fashionme.interactor;

import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.db.ChecklistConstructor;
import mx.app.fashionme.db.DataBase;
import mx.app.fashionme.interactor.interfaces.IChecklistInteractor;
import mx.app.fashionme.pojo.Characteristic;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.pojo.Subcategory;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChecklistInteractor implements IChecklistInteractor {
    
    private static final String TAG = ChecklistInteractor.class.getSimpleName();
    
    @Override
    public void getDataFromAPi(Context context, int idJourney, ChecklistListener listener) {

        RestApiAdapter restApiAdapter = new RestApiAdapter();

        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<Base<Journey>> responseCall = endpointsApi.getJourneyById(idJourney);
        
        responseCall.enqueue(new Callback<Base<Journey>>() {
            @Override
            public void onResponse(Call<Base<Journey>> call, Response<Base<Journey>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")){
                        try {
                            listener.onError(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            listener.onError("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onError(apiError != null ? apiError.getError() : "Algo salió mal");
                        return;
                    }

                }
                
                ArrayList<Characteristic> list = new ArrayList<>();

                for (Subcategory sc: response.body().getData().getSubcategories().getData()) {

                    if (isChecked(idJourney, sc.getId(), context)){
                        list.add(new Characteristic(sc.getId(), sc.getSpanish(), sc.getEnglish(), true));
                    } else {
                        list.add(new Characteristic(sc.getId(), sc.getSpanish(), sc.getEnglish(), false));
                    }
                }

                listener.onGetJourney(list);
                
            }

            @Override
            public void onFailure(Call<Base<Journey>> call, Throwable t) {
                t.printStackTrace();
                listener.onError(t.getMessage());
            }
        });
    }

    private boolean isChecked(int idJourney, int idItem, Context context) {
        ChecklistConstructor constructor = new ChecklistConstructor(context);
        return constructor.checkedItem(new DataBase(context), idJourney, idItem);
    }

    @Override
    public void saveChecklist(ListView listView, int idJourney, Context context, ChecklistListener listener) {

        ChecklistConstructor constructor = new ChecklistConstructor(context);

        constructor.checklistDeleteAll(new DataBase(context), idJourney);

        int countChecklist = listView.getAdapter().getCount();
        int ids[] = new int[countChecklist];

        for (int i = 0; i < countChecklist; i++) {
            CheckBox checkBox = listView.getAdapter().getView(i, null, listView).findViewById(R.id.chkSelection);
            if (checkBox.isChecked()) {
                Characteristic characteristic = (Characteristic) listView.getAdapter().getItem(i);
                ids[i] = characteristic.getId();
            }
        }

        for (int id : ids) {
            if (id != 0) {
                constructor.checklistInsert(new DataBase(context), idJourney, id);
            }
        }

        listener.onChecklistSaved();
    }
}

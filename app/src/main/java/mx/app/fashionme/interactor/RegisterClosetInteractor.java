package mx.app.fashionme.interactor;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IRegisterClosetInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.DataRegisterClotheViewModel;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import mx.app.fashionme.utils.RealPathUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterClosetInteractor implements IRegisterClosetInteractor {

    @Override
    public void setThumbnailFromGallery(Activity activity, Uri data, RegisterClosetListener listener) {
        if (data == null) {
            listener.onError("Error al cargar la imagen");
        }

        listener.onGetThumbnail(data);
    }

    @Override
    public String getRealPathFromURI(Context context, Uri data) {
        return RealPathUtil.getRealPath(context, data);
    }

    @Override
    public void setThumbnail(Context context, Uri uri, RegisterClosetListener listener) {
        listener.onGetThumbnail(uri);
    }

    @Override
    public void register(Context context, String name, String photo, ArrayList<DataRegisterClotheViewModel> subcategories, ArrayList<DataRegisterClotheViewModel> colors, ArrayList<DataRegisterClotheViewModel> dressCodes, ArrayList<DataRegisterClotheViewModel> climates, RegisterClosetListener listener) {
        if (name != null) {
            if (name.isEmpty()) {
                listener.onErrorName(context.getString(R.string.error_field_required));
                return;
            }
        }

        if (photo == null) {
            listener.onError("No has seleccionado una foto");
            return;
        }

        if (dressCodes == null) {
            listener.onError("No has seleccionado codigos de vestimenta");
            return;
        } else {
            if (dressCodes.size() == 0) {
                listener.onError("No has seleccionado codigos de vestimenta");
                return;
            }
        }

        if (climates == null) {
            listener.onError("No has seleccionado climas");
            return;
        } else {
            if (climates.size() == 0) {
                listener.onError("No has seleccionado climas");
                return;
            }
        }

        if (subcategories == null) {
            listener.onError("No has seleccionado subcategorias");
            return;
        } else {
            if (subcategories.size() == 0) {
                listener.onError("No has seleccionado subcategorias");
                return;
            }
        }

        if (colors == null) {
            listener.onError("No has seleccionado colores");
            return;
        } else {
            if (colors.size() == 0) {
                listener.onError("No has seleccionado colores");
                return;
            }
        }

        int userId = SessionPrefs.get(context).getUserId();

        File file = new File(photo);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("imagen", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody nameField = RequestBody.create(MultipartBody.FORM, name != null ? name:"S/N");

        // add dress code part within the multipart request
        ArrayList<Integer> idsDress = new ArrayList<>();
        for (DataRegisterClotheViewModel dress:dressCodes) {
            if (dress.getId() != 0)
                idsDress.add(dress.getId());
        }


        // add climate part within the multipart request
        ArrayList<Integer> idsClimate = new ArrayList<>();
        for (DataRegisterClotheViewModel climate:climates) {
            if (climate.getId() != 0)
                idsClimate.add(climate.getId());
        }

        // add subcategory part within the multipart request
        ArrayList<Integer> idsSubcategory = new ArrayList<>();
        for (DataRegisterClotheViewModel subcategory:subcategories) {
            if (subcategory.getId() != 0)
                idsSubcategory.add(subcategory.getId());
        }

        // add color part within the multipart request
        ArrayList<Integer> idsColor = new ArrayList<>();
        for (DataRegisterClotheViewModel color:colors) {
            if (color.getId() != 0)
                idsColor.add(color.getId());
        }

        // finally, execute the request
        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<Clothe>> call = endpointsApi.registerClotheByUser(
                nameField,
                body,
                idsDress,
                idsClimate,
                idsSubcategory,
                idsColor,
                userId
        );

        call.enqueue(new Callback<Base<Clothe>>() {
            @Override
            public void onResponse(Call<Base<Clothe>> call, Response<Base<Clothe>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                            listener.onError("Error en el servidor, intenta mas tarde");
                            return;
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        listener.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }

                listener.onSuccessUpload();
            }

            @Override
            public void onFailure(Call<Base<Clothe>> call, Throwable t) {
                Log.e("RegisterCloset", "onFailure", t);
                listener.onError("Ocurrio un error, intenta mas tarde");
            }
        });
    }
}

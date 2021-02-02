package mx.app.fashionme.interactor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import mx.app.fashionme.interactor.interfaces.IBodyInteractor;
import mx.app.fashionme.pojo.APIBody;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.utils.RealPathUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heriberto on 20/03/18.
 */

public class BodyInteractor implements IBodyInteractor {

    private static final String TAG = BodyInteractor.class.getSimpleName();

    private byte[] imageBytes;

    @SuppressLint("StaticFieldLeak")
    @Override
    public void sendToClarifai(final Uri uri, final Context context, final View v, final BodyListener listener) {

        if (uri == null){
            listener.onError(v, "No se ha seleccionado una imagen");
            return;
        }

        imageBytes = retrieveSelectedImage(context, uri);

        if (imageBytes == null) {
            listener.onError(v, "Error al cargar la imagen, intenta de nuevo más tarde");
            return;
        }

        listener.onSendingRequest(v);

        // Send photo to
        final ClarifaiClient client = new ClarifaiBuilder("d7fe86b713d14c5d8c17281f979b1cdf")
                .buildSync();

        try {

            new AsyncTask<Void, Void, ClarifaiResponse<List<ClarifaiOutput<Concept>>>>() {
                @Override
                protected ClarifaiResponse<List<ClarifaiOutput<Concept>>> doInBackground(Void... voids) {
                    // The Clarifai model that identifies concepts in images
                    ConceptModel bodyModel = client.getModelByID("Fashion Me").executeSync().get().asConceptModel();
                    // Use model to predict, with the image that the user just selected as the input
                    return bodyModel.predict()
                            .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(imageBytes)))
                            .executeSync();
                }

                @Override
                protected void onPostExecute(ClarifaiResponse<List<ClarifaiOutput<Concept>>> response) {
                    if (!response.isSuccessful()) {
                        SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.UNSENT_PHOTO);
                        Log.e("APIERROR", response.getStatus().toString());
                        listener.onError(v,"Error al reconocer tu cuerpo. Intenta enviar la foto más tarde. Código: " + response.getStatus().statusCode());
                        return;
                    }
                    List<ClarifaiOutput<Concept>> predictions = response.get();
                    if (predictions.isEmpty()) {
                        SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.UNSENT_PHOTO);
                        listener.onError(v, "No hubo resultados de tu cuerpo, intenta con otra imagen");
                        return;
                    }
                    Log.d("API RESPONSE", predictions.get(0).data().toString());
                    SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.SENDING_PHOTO);
                    listener.onSuccess(predictions.get(0).data(), v);
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(v, "Ocurrio un error, intenta más tarde");
            SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.UNSENT_PHOTO);
        }

    }

    @Override
    public void sendToAPI(Context context, String photoPath, View view, BodyListener listener) {
        if (photoPath == null) {
            listener.onError(view, "No ha seleccionado una imagen");
            return;
        }

        String genre = SessionPrefs.get(context).getGenre();
        File file = new File(photoPath);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("img", file.getName(), requestFile);

        // add another part within the multipart request
        RequestBody sex = RequestBody.create(MultipartBody.FORM, genre);
        listener.onGetBodyType("TIPE", view);

        // finally, execute the request
        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionBodyApi();

        Call<APIBody> apiBodyCall = endpointsApi.getBodyTypeAPIv2(sex, body);

        apiBodyCall.enqueue(new Callback<APIBody>() {
            @Override
            public void onResponse(Call<APIBody> call, Response<APIBody> response) {
                if (!response.isSuccessful()) {
                    listener.onError(view, "Algo salió mal, intenta más tarde");
                    return;
                }

                if (response.body().getStatus().equals("error")) {
                    listener.onError(view, "No se reconocio un cuerpo");
                    return;
                }

                SessionPrefs.get(context).saveTokenBody(
                        response.body().getToken()
                );

                listener.onGetBodyType();

            }

            @Override
            public void onFailure(Call<APIBody> call, Throwable t) {
                t.printStackTrace();
                listener.onError(view, "Red no disponible.");
            }
        });

    }

    @Override
    public byte[] retrieveSelectedImage(@NonNull Context context, Uri uri) {

        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return outputStream.toByteArray();
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    @Override
    public void sendToFashion(final String currentBody, final Context context, final View view, final OnRegisterBodyFinished callback) {

//        int userId = SessionPrefs.get(context).getUserId();
//
//        BodyType bodyType = new BodyType();
//
//        switch (currentBody) {
//            case "Oval":
//                bodyType.setName("manzana");
//                break;
//            case "Triangulo":
//                bodyType.setName("triangulo");
//                break;
//            case "Columna":
//                bodyType.setName("columna");
//                break;
//            case "Reloj Arena":
//                bodyType.setName("reloj_arena");
//                break;
//            case "Triangulo Invertido":
//                bodyType.setName("triangulo_invertido");
//                break;
//            case "Trapecio":
//                bodyType.setName("trapecio");
//                break;
//            default:
//                bodyType.setName("columna");
//                break;
//        }
//
//        RestApiAdapter restApiAdapter = new RestApiAdapter();
//        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
//        Call<Base<User>> bodyResponse = endpointsApi.registerBody(bodyType, userId);
//
//        bodyResponse.enqueue(new Callback<Base<User>>() {
//            @Override
//            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
//                if (!response.isSuccessful()) {
//                    if (response.errorBody().contentType().type().equals("text")){
//                        try {
//                            SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.UNSENT_PHOTO);
//                            callback.onErrorRegister(view, response.errorBody().string());
//                            return;
//                        } catch (IOException e) {
//                            SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.UNSENT_PHOTO);
//                            e.printStackTrace();
//                            callback.onErrorRegister(view, "Algo salió mal, intenta más tarde");
//                            return;
//                        }
//                    } else {
//                        SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.UNSENT_PHOTO);
//                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
//                        callback.onErrorRegister(view, apiError != null ? apiError.getError() : "Algo salió mal");
//                        return;
//                    }
//                }
//
//                SessionPrefs.get(context).saveBody(
//                        response.body().getData().getBody().getData().getSpanish().getBody_type(),
//                        response.body().getData().getBody().getData().getEnglish().getBody_type(),
//                        response.body().getData().getBody().getData().getUrlImage());
//                SessionPrefs.get(context).saveStatusPhoto(SessionPrefs.SENT_PHOTO);
//                callback.onSuccessRegister();
//            }
//
//            @Override
//            public void onFailure(Call<Base<User>> call, Throwable t) {
//                t.printStackTrace();
//                SessionPrefs.get(context).saveBody(null, null, null);
//                callback.onErrorRegister(view, "Algo salió mal, intenta más tarde.");
//            }
//        });

    }

    @Override
    public void checkVersion(BodyListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listener.onCheckVersion(true);
        } else {
            listener.onCheckVersion(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void checkPermissions(Activity activity, BodyListener listener) {

        int statusPermissionCamera  = activity.checkSelfPermission(Manifest.permission.CAMERA);
        int statusPermissionSTORAGE = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (statusPermissionCamera != PackageManager.PERMISSION_GRANTED || statusPermissionSTORAGE != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                listener.onCheckPermissions(true);
                return;
            } else if (activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                listener.onCheckPermissions(true);
                return;
            } else {
                listener.onCheckPermissions(false);
                return;
            }
        }

        listener.onCheckPermissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void checkShowExplanation(Activity activity, Context context, BodyListener listener) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, "Como denegaste el acceso al almacenamiento, no se mostrará ninguna foto.", Toast.LENGTH_LONG).show();
        } else if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(activity, "Como denegaste el acceso al almacenamiento, no se mostrará ninguna foto.", Toast.LENGTH_LONG).show();
        } else {
            listener.onCheckExplanation();
        }
    }

    @Override
    public void setThumbnail(Context context, Intent data, Uri uri, BodyListener listener) {
        listener.onGetThumbnail(uri);
    }

    @Override
    public void setThumbnailFromGallery(Activity activity, Uri uri, View v, BodyListener listener) {
        if (uri == null) {
            listener.onError(v, "Error al cargar la imagen");
        }

        listener.onGetThumbnail(uri);
    }

    @Override
    public String getRealPathFromURI(Context context, Uri uri) {
        return RealPathUtil.getRealPath(context, uri);
    }

}

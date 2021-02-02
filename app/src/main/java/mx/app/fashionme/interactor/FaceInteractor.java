package mx.app.fashionme.interactor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

import mx.app.fashionme.contract.FaceContract;
import mx.app.fashionme.pojo.FaceType;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import mx.app.fashionme.utils.RealPathUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaceInteractor implements FaceContract.FaceInteractor {

    private static final String TAG = FaceInteractor.class.getSimpleName();

    @Override
    public void checkVersion(FaceListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listener.onCheckVersion(true);
        } else {
            listener.onCheckVersion(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void checkPermissions(Activity activity, FaceListener listener) {
        int statusPermissionCamera = activity.checkSelfPermission(Manifest.permission.CAMERA);
        int statusPermissionStorage = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (statusPermissionCamera != PackageManager.PERMISSION_GRANTED || statusPermissionStorage != PackageManager.PERMISSION_GRANTED) {
            if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
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
    public void checkShowExplanation(Activity activity, Context context, FaceListener listener) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, "Como denegaste el acceso al almacenamiento, no se mostrará ninguna foto.", Toast.LENGTH_LONG).show();
        } else if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Toast.makeText(activity, "Como denegaste el acceso a la camara, no se mostrará ninguna foto.", Toast.LENGTH_LONG).show();
        } else {
            listener.onCheckExplanation();
        }
    }

    @Override
    public void setThumbnail(Context context, Intent data, Uri uri, FaceListener listener) {
        listener.onGetThumbnail(null,uri);
    }

    @Override
    public void setThumbnailFromGallery(Activity activity, Uri uri, View v, FaceListener listener) {
        if (uri == null) {
            listener.onError(v, "Error al cargar la imagen");
        }

        listener.onGetThumbnail(null, uri);
    }

    @Override
    public String getRealPathFromURI(Context context, Uri uri) {
        return RealPathUtil.getRealPath(context, uri);
    }

    @Override
    public void sendToAPI(String photoPath, Context context, View v, FaceListener listener) {
        if (photoPath == null) {
            listener.onError(v, "No se ha seleccionado una imagen");
            return;
        }

        // TODO, ENVIAR LA IMAGEN A SERVICIO DE RECONOCIMIENTO DE CARA
        String face;
        try {
            String[] faces = {"Alargado", "Rectangulo", "Redondo", "Cuadrado", "Triangulo invertido",
                    "Corazón", "Diamante", "Triangulo", "Ovalado"};

            Random r = new Random();

            int randomNumber = r.nextInt(faces.length);
            face = faces[randomNumber];
//            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(v, "Se interrumpio el servicio");
            return;
        }

        listener.onGetFaceType(face, v);
        Toast.makeText(context, "Resultado: " + face, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendToFashionMEServer(String face, Context context, View view, FaceListener listener) {

        int userId = SessionPrefs.get(context).getUserId();

        FaceType faceType = new FaceType();

        switch (face) {
            case "Alargado":
                faceType.setName("Alargado");
                break;
            case "Rectangulo":
                faceType.setName("Rectangulo");
                break;
            case "Redondo":
                faceType.setName("Redondo");
                break;
            case "Cuadrado":
                faceType.setName("Cuadrado");
                break;
            case "Triangulo invertido":
                faceType.setName("Triangulo invertido");
                break;
            case "Corazón":
                faceType.setName("Corazón");
                break;
            case "Diamante":
                faceType.setName("Diamante");
                break;
            case "Triangulo":
                faceType.setName("Triangulo");
                break;
            case "Ovalado":
                faceType.setName("Ovalado");
                break;
        }

        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = restApiAdapter.establecerConexionRestApi();
        Call<Base<User>> faceResponse = endpointsApi.registerFace(faceType, userId);

        faceResponse.enqueue(new Callback<Base<User>>() {
            @Override
            public void onResponse(Call<Base<User>> call, Response<Base<User>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            listener.onError(view, response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, "Error register face", e.getCause());
                            listener.onError(view, "Algo salio mal, intenta mas tarde");
                            return;
                        }
                    } else {
                        ApiError error = ApiError.fromResponseBody(response.errorBody());
                        listener.onError(view, error != null ? error.getError() : "Algo salio mal");
                        return;
                    }
                }

                SessionPrefs.get(context).saveFace(
                        response.body().getData().getFace().getData().getSpanish().getFace_name(),
                        response.body().getData().getFace().getData().getEnglish().getFace_name(),
                        response.body().getData().getFace().getData().getUrlImage()
                        );
                listener.onSuccessRegister();

            }

            @Override
            public void onFailure(Call<Base<User>> call, Throwable t) {
                Log.e(TAG, "onFailure", t);
                listener.onError(view, "Error en la conexion");
            }
        });
    }
}

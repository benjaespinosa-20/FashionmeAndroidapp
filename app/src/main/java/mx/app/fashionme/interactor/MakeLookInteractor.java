package mx.app.fashionme.interactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.db.LooksConstructor;
import mx.app.fashionme.interactor.interfaces.IMakeLookInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.English;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.pojo.Spanish;
import mx.app.fashionme.pojo.Subcategory;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import mx.app.fashionme.sticker.StickerImageView;
import mx.app.fashionme.sticker.StickerTextInputDialog;
import mx.app.fashionme.sticker.StickerTextView;
import mx.app.fashionme.sticker.StickerView;
import mx.app.fashionme.utils.FileUtils;
import mx.app.fashionme.view.interfaces.IMakeLookView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heriberto on 11/04/18.
 */

public class MakeLookInteractor implements IMakeLookInteractor {

    public static final String TAG = MakeLookInteractor.class.getSimpleName();

    @Override
    public void getDataSuggestions(Context context, int clotheId, final OnGetSuggestionsFinishedListener callback) {

        //int userId = SessionPrefs.get(context).getUserId();
        String genre = SessionPrefs.get(context).getGenre();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<ArrayList<Clothe>>> call = endpointsApi.getSuggestionsByIdClothe(clotheId, genre);
        call.enqueue(new Callback<Base<ArrayList<Clothe>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Clothe>>> call, Response<Base<ArrayList<Clothe>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            callback.onError(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            callback.onError("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }

                ArrayList<Clothe> clothesSuggestion = new ArrayList<>();

                clothesSuggestion.addAll(response.body().getData());


                Base<ArrayList<Subcategory>> baseSubcategories = new Base<>();
                ArrayList<Subcategory> subcategoryEmoji = new ArrayList<>();
                Subcategory subcategory = new Subcategory(0, new Spanish("Emojis"), new English("Emojis"));
                subcategoryEmoji.add(subcategory);
                baseSubcategories.setData(subcategoryEmoji);

                String urlsEmoji[] = {

                };

                for (int i = 0; i <= 9; i++) {
                    Clothe emoji = new Clothe();
                    emoji.setUrlImage(urlsEmoji[i]);
                    emoji.setSubcategories(baseSubcategories);
                    clothesSuggestion.add(emoji);
                }

                callback.onSuccess(clothesSuggestion);
            }

            @Override
            public void onFailure(Call<Base<ArrayList<Clothe>>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                callback.onError(t.getMessage());
            }
        });
    }

    @Override
    public void getDataSuggestions(Context context, final OnGetSuggestionsFinishedListener callback) {
        String genre = SessionPrefs.get(context).getGenre();

        RestApiAdapter apiAdapter = new RestApiAdapter();
        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<ArrayList<Clothe>>> clotheCall = endpointsApi.getClothes(genre);
        clotheCall.enqueue(new Callback<Base<ArrayList<Clothe>>>() {
            @Override
            public void onResponse(Call<Base<ArrayList<Clothe>>> call, Response<Base<ArrayList<Clothe>>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            callback.onError(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                            callback.onError("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onError(apiError != null ? apiError.getError(): "Algo salió mal, intenta más tarde");
                        return;
                    }
                }

                ArrayList<Clothe> clothesSuggestion = new ArrayList<>();

                clothesSuggestion.addAll(response.body().getData());


                Base<ArrayList<Subcategory>> baseSubcategories = new Base<>();
                ArrayList<Subcategory> subcategoryEmoji = new ArrayList<>();
                Subcategory subcategory = new Subcategory(0, new Spanish("Emojis"), new English("Emojis"));
                subcategoryEmoji.add(subcategory);
                baseSubcategories.setData(subcategoryEmoji);

                String urlsEmoji[] = {
                        "https://cdn.shopify.com/s/files/1/1061/1924/products/Slightly_Smiling_Face_Emoji_87fdae9b-b2af-4619-a37f-e484c5e2e7a4_large.png",
                        "https://cdn.shopify.com/s/files/1/1061/1924/products/Upside-Down_Face_Emoji_4dbbbd80-eb60-4c91-9642-83368692e361_large.png",
                        "https://cdn.shopify.com/s/files/1/1061/1924/products/Smiling_Face_Emoji_large.png",
                        "https://cdn.shopify.com/s/files/1/1061/1924/products/Smiling_Emoji_with_Eyes_Opened_large.png",
                        "https://cdn.shopify.com/s/files/1/1061/1924/products/Smiling_Emoji_with_Smiling_Eyes_large.png",
                        "https://cdn.shopify.com/s/files/1/1061/1924/products/Smiling_Face_with_Tightly_Closed_eyes_large.png",
                        "https://cdn.shopify.com/s/files/1/1061/1924/products/Smiling_with_Sweat_Emoji_large.png",
                        "https://cdn.shopify.com/s/files/1/1061/1924/products/Tears_of_Joy_Emoji_8afc0e22-e3d4-4b07-be7f-77296331c687_large.png",
                        "https://cdn.shopify.com/s/files/1/1061/1924/products/Smirk_Face_Emoji_large.png",
                        "https://cdn.shopify.com/s/files/1/1061/1924/products/Unamused_Face_Emoji_761d8bf8-c78c-45b1-80b1-a86a80d2452d_large.png"
                };

                for (int i = 0; i <= 9; i++) {
                    Clothe emoji = new Clothe();
                    emoji.setUrlImage(urlsEmoji[i]);
                    emoji.setSubcategories(baseSubcategories);
                    clothesSuggestion.add(emoji);
                }

                callback.onSuccess(clothesSuggestion);
            }

            @Override
            public void onFailure(Call<Base<ArrayList<Clothe>>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                callback.onError(t.getMessage());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void createSticker(final String urlImage, Context context, IMakeLookView view, final OnMakeStickerFinishedListener callback) {
        final StickerImageView imageView = new StickerImageView(context, view);

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlImage);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream input = connection.getInputStream();
                    return BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap == null) {
                    callback.onErrorMake("Error al generar la imagen");
                    return;
                }
                imageView.setImageBitmap(bitmap);
                super.onPostExecute(bitmap);
                callback.onSuccessMake(imageView);
            }
        }.execute();
    }

    @Override
    public void createStickerText(String text, Context context, StickerTextInputDialog stickerTextInputDialog, IMakeLookView view, OnMakeStickerFinishedListener callback) {
        Log.d(TAG, "Method: interactor creatStickerText");
        StickerTextView stickerTextView = new StickerTextView(context, stickerTextInputDialog, view);

        stickerTextView.setText(text);
        callback.onSuccessMake(stickerTextView);
    }

    @Override
    public void saveLook(View view, Context context, ArrayList<StickerView> stickerViews, boolean showCalendar, OnSavedFileListener callback) {

        if (stickerViews.size() == 0) {
            callback.onErrorSave("Debes elegir al menos un elemento");
            return;
        }

        File file = FileUtils.getNewFile(context, "Fashion Me");

        if (file != null) {
            save(file, view, context);
            Look look = new Look();
            look.setUri(file.getAbsolutePath());
            saveToDB(context, look);
            callback.onSuccessSave(context.getString(R.string.look_saved), showCalendar, look);
        } else {
            callback.onErrorSave("The file is null");
        }
    }

    @Override
    public void save(File file, View view, Context context) {
        FileUtils.saveImageToGallery(file, createBitmap(view));
        FileUtils.notifySystemGallery(context, file);
    }

    @Override
    public void saveToDB(Context context, Look look) {
        LooksConstructor looksConstructor = new LooksConstructor(context);
        look.setId((int) looksConstructor.saveLook(look));
    }

    @Override
    public Bitmap createBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.setBackgroundColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    @Override
    public void filterSuggestions(ArrayList<Clothe> currentSuggestions, String filter, Context context, OnFilterFinishedListener listener) {
        ArrayList<Clothe> filtered = new ArrayList<>();
        String lang = context.getString(R.string.app_language);


        for (Clothe clothe : currentSuggestions) {
            switch (lang) {
                case "spanish":
                    for (Subcategory subcategory : clothe.getSubcategories().getData()) {
                        if (subcategory.getSpanish().getName().equals(filter))
                            filtered.add(clothe);
                    }
                    break;
                case "english":
                    for (Subcategory subcategory : clothe.getSubcategories().getData()) {
                        if (subcategory.getEnglish().getName().equals(filter)) {
                            filtered.add(clothe);
                        }
                    }
                    break;
                default:
                    for (Subcategory subcategory : clothe.getSubcategories().getData()) {
                        if (subcategory.getSpanish().getName().equals(filter))
                            filtered.add(clothe);
                    }
                    break;
            }
        }

        listener.onFilter(filtered);
    }

    @Override
    public void saveDateToLook(Context context, Look look, String date, OnSavedDateFinishedListener listener) {
        if (date == null) {
            listener.onErrorSavedDate("Debes elegir una fecha.", look);
            return;
        }

        look.setDate(date);
        LooksConstructor constructor = new LooksConstructor(context);
        try {
            constructor.updateDateLook(look);
            listener.onSuccessSavedDate("Look agregado al calendario");
        } catch (Exception e) {
            e.printStackTrace();
            listener.onErrorSavedDate(e.getMessage(), look);
        }
    }
}

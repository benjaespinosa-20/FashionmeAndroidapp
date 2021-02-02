package mx.app.fashionme.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import mx.app.fashionme.pojo.User;

/**
 * Created by heriberto on 14/03/18.
 */

public class SessionPrefs {
    public static final String PREFS_FILE_NAME = "FILE_FASHION_PREFS";

    public static final String KEY_USER_ID              = "USER_ID";
    public static final String KEY_USER_NAME            = "USER_NAME";
    public static final String KEY_USER_EMAIL           = "USER_EMAIL";
    public static final String KEY_USER_GENDER          = "USER_GENDER";
    public static final String KEY_USER_ROLE            = "USER_ROLE";
    public static final String KEY_USER_TOKEN           = "USER_TOKEN";


    public static final String KEY_BODY_TYPE_SPANISH    = "BODY_TYPE_SPANISH";
    public static final String KEY_BODY_TYPE_ENGLISH    = "BODY_TYPE_ENGLISH";
    public static final String KEY_BODY_SPECIFICATIONS  = "BODY_SPECIFICATIONS";
    public static final String KEY_FACE_TYPE_SPANISH    = "FACE_TYPE_SPANISH";
    public static final String KEY_FACE_TYPE_ENGLISH    = "FACE_TYPE_ENGLISH";
    public static final String KEY_FACE_TYPE_URL        = "FACE_TYPE_IMAGE_URL";

    public static final String KEY_TOKEN_BODY_API       = "BODY_API_TOKEN";

    public static final String KEY_TEST_COLOR_SPANISH   = "COLOR_SPANISH";
    public static final String KEY_TEST_COLOR_ENGLISH   = "COLOR_ENGLISH";
    private static final String KEY_COLOR_IMAGE         = "COLOR_IMAGE_URL";
    public static final String KEY_COLOR_EXTRA          = "KEY_COLOR_EXTRA";

    public static final String KEY_RESULT               = "KEY_RESULT";

    public static final String KEY_STATUS_PHOTO         = "KEY_STATUS_PHOTO";
    public static final int UNSENT_PHOTO                = -1;
    public static final int SENDING_PHOTO               = 0;
    public static final int SENT_PHOTO                  = 1;

    public static final String KEY_USER_STYLE_SPANISH   = "KEY_USER_STYLE_SPANISH";
    public static final String KEY_USER_STYLE_ENGLISH   = "KEY_USER_STYLE_ENGLISH";

    public static  final String KEY_CLOSET_IDEAL       = "KEY_CLOSET_IDEAL";
    private static final String KEY_STATUS_TOKEN_ASSOCIATED = "KEY_TOKEN_ASSOCIATED";
    private static final String KEY_ACTIVITY_RESULT = "KEY_RESULT_ACTIVITY";

    private static SessionPrefs INSTANCE;
    private final SharedPreferences prefs;

    private boolean isLoggedIn                  = false;
    private boolean isBodyRegister              = false;
    private boolean isFaceRegister              = false;
    private boolean isSpecificationsRegister    = false;
    private boolean isTestCompleted             = false;
    private boolean isExtraTestCompleted        = false;
    private boolean isShownResult               = false;

    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    private SessionPrefs(Context context){
        prefs                       = context.getApplicationContext().getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);

        // TODO: Change LoggedIn for token
        isLoggedIn                  = !TextUtils.isEmpty(prefs.getString(KEY_USER_NAME, null));
        isBodyRegister              = !TextUtils.isEmpty(prefs.getString(KEY_BODY_TYPE_SPANISH, null));
        isFaceRegister              = !TextUtils.isEmpty(prefs.getString(KEY_FACE_TYPE_SPANISH, null));
        isSpecificationsRegister    = prefs.getBoolean(KEY_BODY_SPECIFICATIONS, false);
        isTestCompleted             = !TextUtils.isEmpty(prefs.getString(KEY_TEST_COLOR_SPANISH, null));
        isExtraTestCompleted        = prefs.getBoolean(KEY_COLOR_EXTRA, false);
        isShownResult               = prefs.getBoolean(KEY_RESULT, false);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isBodyRegister() {
        return isBodyRegister;
    }

    public boolean isFaceRegister() {
        return isFaceRegister;
    }

    public boolean isSpecificationsRegister() {
        return isSpecificationsRegister;
    }

    public boolean isTestCompleted() {
        return isTestCompleted;
    }

    public boolean isExtraTestCompleted() {
        return isExtraTestCompleted;
    }

    public boolean isShownResult() {
        return isShownResult;
    }

    public void saveUser(User user) {
        if (user != null) {
            SharedPreferences.Editor editor = prefs.edit();

            editor.putInt(KEY_USER_ID, user.getId());
            editor.putString(KEY_USER_NAME, user.getName());
            editor.putString(KEY_USER_EMAIL, user.getEmail());
            editor.putString(KEY_USER_GENDER, user.getGenre());
            editor.putString(KEY_USER_ROLE, user.getRole());
            editor.putString(KEY_USER_TOKEN, user.getToken());

            if (user.getBody() != null) {
                editor.putString(KEY_BODY_TYPE_SPANISH, user.getBody().getData().getSpanish().getBody_type());
                editor.putString(KEY_BODY_TYPE_ENGLISH, user.getBody().getData().getEnglish().getBody_type());
            }

            if (user.getColor() != null) {
                editor.putString(KEY_TEST_COLOR_SPANISH, user.getColor().getData().getSpanish().getColor_name());
                editor.putString(KEY_TEST_COLOR_ENGLISH, user.getColor().getData().getEnglish().getColor_name());
            }


            editor.apply();

            isLoggedIn = true;
        }
    }

    public void saveBody(String bodyTypeSpanish, String bodyTypeEnglish, String urlImage) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_BODY_TYPE_SPANISH, bodyTypeSpanish);
        editor.putString(KEY_BODY_TYPE_ENGLISH, bodyTypeEnglish);
        editor.apply();

        isBodyRegister = true;
    }

    public void saveFace(String faceTypeSpanish, String faceTypeEnglish, String urlImage) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_FACE_TYPE_SPANISH, faceTypeSpanish);
        editor.putString(KEY_FACE_TYPE_ENGLISH, faceTypeEnglish);
        editor.putString(KEY_FACE_TYPE_URL, urlImage);
        editor.apply();

        isFaceRegister = true;
    }

    public void saveCharacteristics() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_BODY_SPECIFICATIONS, true);
        editor.apply();

        isSpecificationsRegister = true;
    }

    public void saveColor(String colorSpanish, String colorEnglish, String imageUrl) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TEST_COLOR_SPANISH, colorSpanish);
        editor.putString(KEY_TEST_COLOR_ENGLISH, colorEnglish);
        editor.putString(KEY_COLOR_IMAGE, imageUrl);
        editor.apply();

        isTestCompleted = true;
    }

    public void saveExtraColorTest(boolean test) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_COLOR_EXTRA, test);
        editor.apply();
        isExtraTestCompleted = true;
    }

    public void saveResult(boolean save) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_RESULT, save);
        editor.apply();

        isShownResult = save;
    }

    public void saveStatusPhoto(int status) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_STATUS_PHOTO, status);
        editor.apply();
    }

    public void saveStyleUser(String styleSpanish, String styleEnglish) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USER_STYLE_SPANISH, styleSpanish);
        editor.putString(KEY_USER_STYLE_ENGLISH, styleEnglish);
        editor.apply();
    }

    public void logOut(Context context) {
        isLoggedIn = false;
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Borrar las preferencias de usuario
        // registradas en el menu configuracion
        SharedPreferences defaultPrefs= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editorDefault = defaultPrefs.edit();
        editorDefault.clear();
        editorDefault.apply();
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, 0);
    }

    public String getBodyType(String lang){
        switch (lang) {
            case "spanish":
                return prefs.getString(KEY_BODY_TYPE_SPANISH, null);
            case "english":
                return prefs.getString(KEY_BODY_TYPE_ENGLISH, null);
                default:
                    return prefs.getString(KEY_BODY_TYPE_SPANISH, null);
        }
    }

    public String getFaceType(String lang) {
        switch (lang) {
            case "spanish":
                return prefs.getString(KEY_FACE_TYPE_SPANISH, null);
            case "english":
                return prefs.getString(KEY_FACE_TYPE_ENGLISH, null);
                default:
                    return prefs.getString(KEY_FACE_TYPE_SPANISH, null);
        }
    }

    public String getFaceImage() {
        return prefs.getString(KEY_FACE_TYPE_URL, null);
    }

    public int getStatusPhoto(){
        return prefs.getInt(KEY_STATUS_PHOTO, -1);
    }

    public String getGenre(){
        return prefs.getString(KEY_USER_GENDER, null);
    }

    public String getRole(){
        return prefs.getString(KEY_USER_ROLE, null);
    }

    public String getColor(String lang) {
        switch (lang) {
            case "spanish":
                return prefs.getString(KEY_TEST_COLOR_SPANISH, null);
            case "english":
                return prefs.getString(KEY_TEST_COLOR_ENGLISH, null);
                default:
                    return prefs.getString(KEY_TEST_COLOR_SPANISH, null);
        }
    }

    public String getColorImage() {
        return prefs.getString(KEY_COLOR_IMAGE, null);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    public String getEmail() {
        return  prefs.getString(KEY_USER_EMAIL, null);
    }


    public String getStyle(String lang) {
        switch (lang) {
            case "spanish":
                return prefs.getString(KEY_USER_STYLE_SPANISH, null);
            case "english":
                return prefs.getString(KEY_USER_STYLE_ENGLISH, null);
            default:
                return prefs.getString(KEY_USER_STYLE_SPANISH, null);
        }
    }

    public void isIdealCloset(boolean idealCloset) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_CLOSET_IDEAL, idealCloset);
        editor.apply();
    }

    public void saveTokenAssociated(boolean status) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_STATUS_TOKEN_ASSOCIATED, status);
        editor.apply();
    }

    public boolean isTokenAssociated() {
        return prefs.getBoolean(KEY_STATUS_TOKEN_ASSOCIATED, false);
    }

    public String getActivity() {
        return prefs.getString(KEY_ACTIVITY_RESULT, null);
    }

    public void saveActivityAfterResult(String name) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ACTIVITY_RESULT, name);
        editor.apply();
    }

    public void saveTokenBody(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TOKEN_BODY_API, token);
        editor.apply();
    }

    public String getTokenBodyApi() {
        return prefs.getString(KEY_TOKEN_BODY_API, null);
    }
}

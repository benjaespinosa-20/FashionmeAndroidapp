package mx.app.fashionme.restApi;

/**
 * Created by heriberto on 13/03/18.
 */

public final class RestApiConstants {

    public static final String ROOT_URL = "http://admin.fashionmeweb.com/api/";
    //public static final String ROOT_URL = "http://ec2-18-232-72-3.compute-1.amazonaws.com/api/";
    //public static final String ROOT_URL = "http://192.168.5.18/api/";
    //public static final String ROOT_URL = "https://8019f3da.ngrok.io/api/";
    public static final String ROOT_URL_LOCAL = "http://admin.fashionmeweb.com/api/";
    //public static final String ROOT_URL_LOCAL = "http://8ec502ce.ngrok.io/api/";


//    public static final String ACCESS_TOKEN = "";

    public static final String ROOT_URL_OPENWEATHER = "https://api.openweathermap.org/data/2.5/";
    public static final String ROOT_URL_BODY_API = "http://fashion.fayweb.mx/";
    public static final String ROOT_URL_BODY_API_V2 = "http://api.fashionmeweb.com/";

    // Login
    static final String URL_LOGIN = "login";

    // Register
    static final String URL_REGISTER = "users";

    // Users
    public static final String URL_GET_USERS = "users";
    public static final String URL_GET_USER = "users/{user}";
    public static final String URL_GET_PERSONAL_CLOTHES_BY_USER = "users/{user}/owner-clothes";

    // Categories
    static final String URL_GET_CATEGORIES_BOTH_GENRE = "categories?genre=b";
    static final String URL_GET_CATEGORIES_WOMAN_GENRE = "categories?genre=w";
    static final String URL_GET_CATEGORIES_MAN_GENRE = "categories?genre=m";

    // Subcategories
    static final String URL_GET_SUBCATEGORIES = "categories/{id}/subcategories";
    static final String URL_GET_SUBCATEGORIES_ALL = "subcategories";

    // Clothes
    static final String URL_GET_CLOTHES_ALL = "clothes";
    static final String URL_GET_CLOTHES = "subcategories/{subcategory}/users/{user}/clothes";
    static final String URL_GET_CLOTHES_WOMAN = "subcategories/{subcategory}/users/{user}/clothes?genre=woman";
    static final String URL_GET_CLOTHES_WOMAN_ALL = "subcategories/{subcategory}/clothes?genre=woman";
    static final String URL_GET_CLOTHES_MAN = "subcategories/{subcategory}/users/{user}/clothes?genre=man";
    static final String URL_GET_CLOTHES_MAN_ALL = "subcategories/{subcategory}/clothes?genre=man";
    static final String URL_REGISTER_CLOTHE_BY_USER = "users/{user}/owner-clothes";


    // Characteristics
    static final String URL_GET_CHARACTERISTICS = "characteristics";
    static final String URL_SEND_CHARACTERISTICS = "users/{user}/characteristics";

    // Colorimetria
    static final String  URL_SEND_TEST = "users/{user}/colors";
    // Extra
    static final String URL_SEND_EXTRA_TEST = "users/{user}/extra-color";
    static final String URL_GET_COLORS = "colors";

    // Trend
    public static final String URL_GET_TREND = "trends";
    public static final String URL_GET_TREND_BY_ID = "trends/{trend}";

    // Dress code
    public static final String URL_GET_WAYS_DRESSING = "ways-dressing";
    public static final String URL_GET_DRESS_CODE_BY_ID = "ways-dressing/{id}";
    public static final String URL_GET_DRESS_CODES = "dress-codes";

    // Tips
    public static final String URL_GET_TIP = "tips";
    public static final String URL_GET_TIP_BY_ID = "tips/{tip}";

    // Register body type
    public static final String URL_POST_BODY = "users/{user}/bodies";


    // Questions Style Test
    public static final String URL_GET_QUESTIONS_STYLE_WOMAN = "style-questions?genre=woman";
    public static final String URL_GET_QUESTIONS_STYLE_MAN = "style-questions?genre=man";

    // Send style test
    public static final String URL_SEND_STYLE_TEST = "users/{user}/styles";

    // Get suggestions by id clothe
    public static final String URL_GET_SUGGESTIONS_BY_ID_CLOTHE = "clothes/{clothe}/suggestions";

    // Get weather by OpenWeather
    public static final String URL_GET_WEATHER = "weather";

    // Get recommendations
    public static final String URL_GET_RECOMMENDATIONS = "users/{user}/recommendations";
    public static final String URL_GET_CLOSET_PERSONAL_RECOMMENDATIONS = "users/{user}/owner-clothe-recommendations";

    // Post API BODY
    public static final String URL_POST_BODY_API = "cuerpo.php";
    public static final String URL_POST_BODY_API_V2 = "cuerpo";
    public static final String URL_GET_RESPUESTA_API_V2 = "respuesta";

    // Favorites
    public static final String URL_GET_FAV_BY_USER_BY_ID_TYPE = "users/{user}/favorites/{idType}";
    public static final String URL_POST_FAV_BY_USER = "users/{user}/favorites";
    public static final String URL_DELETE_FAV_BY_USER = "users/{user}/favorites/{idType}";
    public static final String URL_GET_FAVORITES_BY_USER = "users/{user}/favorites";

    // Journeys
    public static final String URL_GET_JOURNEYS = "journeys";
    public static final String URL_GET_JOURNEY_BY_ID = "journeys/{journey}";

    // Chats

    public static final String URL_POST_IMAGE_CHAT = "users/{user}/chats/uploadImage";
    public static final String URL_GET_UID_CHAT = "users/{user}/chats";
    public static final String URL_POST_REGISTERUID_CHAT = "users/{user}/chats";

    public static final String URL_PUT_CHAT_ASSESSOR = "chats/{chat}";

    public static final String URL_GET_CHATS = "chats";

    // Token devices
    public static final String URL_POST_REGISTER_TOKEN = "tokens";
    public static final String URL_PUT_TOKENS = "tokens/{token}";
    public static final String URL_POST_REGISTER_TOKEN_BY_USER = "users/{user}/tokens";

    // Face by user
    public static final String URL_POST_USER_FACE = "users/{user}/faces";

    // Visage
    public static final String URL_GET_VISAGES_BY_USER = "users/{user}/visages";

    // Makeups
    public static final String URL_GET_MAKEUPS_BY_USER = "users/{user}/makeups";

    // Hair
    public static final String URL_GET_HAIR_BY_USER = "users/{user}/hairs";

    // Premium update
    public static final String URL_UPDATE_PREMIUM = "users/{user}";

    // Recuperar password
    public static final String URL_RECOVER_PASSWORD = "password-recover";

    // Preguntas de colorimetria
    public static final String URL_GET_QUESTIONS_COLOR = "color-questions";

    // Clima
    public static final String URL_GET_CLIMATES = "climates";
}

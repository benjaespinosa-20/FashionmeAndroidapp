package mx.app.fashionme.restApi;

import java.util.ArrayList;
import java.util.Map;

import mx.app.fashionme.pojo.APIBody;
import mx.app.fashionme.pojo.Answer;
import mx.app.fashionme.pojo.AnswerStyle;
import mx.app.fashionme.pojo.BodyType;
import mx.app.fashionme.pojo.Category;
import mx.app.fashionme.pojo.Characteristic;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.pojo.Climate;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.Colorimetry;
import mx.app.fashionme.pojo.DressCode;
import mx.app.fashionme.pojo.FaceType;
import mx.app.fashionme.pojo.Favorite;
import mx.app.fashionme.pojo.Forgot;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.pojo.Makeup;
import mx.app.fashionme.pojo.Premium;
import mx.app.fashionme.pojo.QuestionColor;
import mx.app.fashionme.pojo.QuestionStyle;
import mx.app.fashionme.pojo.Subcategory;
import mx.app.fashionme.pojo.Tip;
import mx.app.fashionme.pojo.Trend;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.pojo.Visage;
import mx.app.fashionme.pojo.WayDressing;
import mx.app.fashionme.pojo.WeatherResp;
import mx.app.fashionme.pojo.firebase.ChatClient;
import mx.app.fashionme.pojo.firebase.DeviceToken;
import mx.app.fashionme.restApi.model.BaseLogin;
import mx.app.fashionme.restApi.model.CharacteristicResponse;
import mx.app.fashionme.restApi.model.Base;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by heriberto on 12/03/18.
 */

public interface EndpointsApi {

//    @POST("login")
//    Call<User> login(@Body LoginBody loginBody);

    @GET(RestApiConstants.URL_GET_CATEGORIES_BOTH_GENRE)
    Call<Base<ArrayList<Category>>> getCategoriesBothGenre();

    @GET(RestApiConstants.URL_GET_CATEGORIES_WOMAN_GENRE)
    Call<Base<ArrayList<Category>>> getCategoriesWomanGenre();

    @GET(RestApiConstants.URL_GET_CATEGORIES_MAN_GENRE)
    Call<Base<ArrayList<Category>>> getCategoriesManGenre();

    @GET(RestApiConstants.URL_GET_SUBCATEGORIES)
    Call<Base<ArrayList<Subcategory>>> getSubcategories(@Path("id") int categoryId);

    @GET(RestApiConstants.URL_GET_CLOTHES)
    Call<Base<ArrayList<Clothe>>> getClothes(@Path("subcategory") int subcategoryId, @Path("user") int userId);

    @GET(RestApiConstants.URL_GET_CLOTHES_ALL)
    Call<Base<ArrayList<Clothe>>> getClothes(@Query("genre") String genre);

    @GET(RestApiConstants.URL_GET_CLOTHES_WOMAN)
    Call<Base<ArrayList<Clothe>>> getClothesWoman(@Path("subcategory") int subcategoryId, @Path("user") int userId);

    @GET(RestApiConstants.URL_GET_CLOTHES_WOMAN_ALL)
    Call<Base<ArrayList<Clothe>>> getClothesWoman(@Path("subcategory") int subcategoryId);

    @GET(RestApiConstants.URL_GET_CLOTHES_MAN)
    Call<Base<ArrayList<Clothe>>> getClothesMan(@Path("subcategory") int subcategoryId, @Path("user") int userId);

    @GET(RestApiConstants.URL_GET_CLOTHES_MAN_ALL)
    Call<Base<ArrayList<Clothe>>> getClothesMan(@Path("subcategory") int subcategoryId);

    @POST(RestApiConstants.URL_LOGIN)
    Call<BaseLogin<User>> login(@Body User user);

    @POST(RestApiConstants.URL_REGISTER)
    Call<BaseLogin<User>> register(@Body User user);

    @GET(RestApiConstants.URL_GET_CHARACTERISTICS)
    Call<Base<ArrayList<Characteristic>>> getCharacteristics();

    @FormUrlEncoded
    @POST(RestApiConstants.URL_SEND_CHARACTERISTICS)
    Call<CharacteristicResponse> sendCharacteristics(@Field("characteristics[]")ArrayList<Integer> ids, @Path("user")int userId);

    @POST(RestApiConstants.URL_SEND_TEST)
    Call<Base<User>> sendTest(@Body Answer answer, @Path("user") int userId);

    @POST(RestApiConstants.URL_SEND_EXTRA_TEST)
    Call<Base<User>> sendExtraTest(@Body Answer answer, @Path("user") int userId);

    @GET(RestApiConstants.URL_GET_TREND)
    Call<Base<Trend>> getTrend();

    @GET(RestApiConstants.URL_GET_TREND_BY_ID)
    Call<Base<Trend>> getTrendById(@Path("trend") int idTrend);

    @GET(RestApiConstants.URL_GET_DRESS_CODE_BY_ID)
    Call<Base<WayDressing>> getDressCodeById(@Path("id") int wayDressing);

    @GET(RestApiConstants.URL_GET_TIP)
    Call<Base<Tip>> getTip();

    @GET(RestApiConstants.URL_GET_TIP_BY_ID)
    Call<Base<Tip>> getTipById(@Path("tip") int idTip);

    @POST(RestApiConstants.URL_POST_BODY)
    Call<Base<User>> registerBody(@Body BodyType bodyType, @Path("user") int userId);

    @GET(RestApiConstants.URL_GET_USER)
    Call<Base<User>> getUserById(@Path("user") int userId);

    @GET(RestApiConstants.URL_GET_QUESTIONS_STYLE_WOMAN)
    Call<Base<ArrayList<QuestionStyle>>> getQuestionsStyleWoman();

    @GET(RestApiConstants.URL_GET_QUESTIONS_STYLE_MAN)
    Call<Base<ArrayList<QuestionStyle>>> getQuestionsStyleMan();

    @POST(RestApiConstants.URL_SEND_STYLE_TEST)
    Call<Base<User>> sendStyleTest(@Body AnswerStyle answerStyle, @Path("user") int userId);

    @GET(RestApiConstants.URL_GET_SUGGESTIONS_BY_ID_CLOTHE)
    Call<Base<ArrayList<Clothe>>> getSuggestionsByIdClothe(@Path("clothe") int idClothe, @Query("genre") String genre);

    @GET(RestApiConstants.URL_GET_WAYS_DRESSING)
    Call<Base<ArrayList<WayDressing>>> getWaysDressing();

    // Weather request
    @GET(RestApiConstants.URL_GET_WEATHER)
    Call<WeatherResp> getWeather(@QueryMap Map<String, String> options);

    // Recommendations
    @GET(RestApiConstants.URL_GET_RECOMMENDATIONS)
    Call<Base<ArrayList<Clothe>>> getRecommendations(@Path("user") int userId, @QueryMap Map<String, String> options);

    // API Body Type
    @Multipart
    @POST(RestApiConstants.URL_POST_BODY_API)
    Call<String> getBodyTypeAPI(@Part("sex") RequestBody sex, @Part MultipartBody.Part img);

    // API Body Type v2
    @Multipart
    @POST(RestApiConstants.URL_POST_BODY_API_V2)
    Call<APIBody> getBodyTypeAPIv2(@Part("sex") RequestBody sex, @Part MultipartBody.Part img);

    @GET(RestApiConstants.URL_GET_RESPUESTA_API_V2)
    Call<APIBody> getRespuestaAPIv2(@Query("token") String token);

    @GET(RestApiConstants.URL_GET_FAV_BY_USER_BY_ID_TYPE)
    Call<Base<Clothe>> getFavoriteByUserByIdClothe(@Path("user") int userId, @Path("idType") int clotheId, @Query("type") String type);

    @GET(RestApiConstants.URL_GET_FAV_BY_USER_BY_ID_TYPE)
    Call<Base<Trend>> getFavoriteByUserByIdTrend(@Path("user") int userId, @Path("idType") int clotheId, @Query("type") String type);

    @GET(RestApiConstants.URL_GET_FAV_BY_USER_BY_ID_TYPE)
    Call<Base<Tip>> getFavoriteByUserByIdTip(@Path("user") int userId, @Path("idType") int idTip, @Query("type") String type);

    @GET(RestApiConstants.URL_GET_FAV_BY_USER_BY_ID_TYPE)
    Call<Base<WayDressing>> getFavoriteByUserByIdWayDressing(@Path("user") int userId, @Path("idType") int idWay, @Query("type") String type);

    @GET(RestApiConstants.URL_GET_FAV_BY_USER_BY_ID_TYPE)
    Call<Base<Journey>> getFavoriteByUSerByIdJourney(@Path("user") int userId, @Path("idType") int idJourney, @Query("type") String type);

    @POST(RestApiConstants.URL_POST_FAV_BY_USER)
    Call<Base<Clothe>> setFavoriteClotheByUser(@Path("user") int userId, @Body Clothe clothe);

    @POST(RestApiConstants.URL_POST_FAV_BY_USER)
    Call<Base<Trend>> setFavoriteTrendByUser(@Path("user") int userId, @Body Trend trend);

    @POST(RestApiConstants.URL_POST_FAV_BY_USER)
    Call<Base<Tip>> setFavoriteTipByUser(@Path("user") int userId, @Body Tip tip);

    @POST(RestApiConstants.URL_POST_FAV_BY_USER)
    Call<Base<WayDressing>> setFavoriteWayDressByUser(@Path("user") int userId, @Body WayDressing wayDressing);

    @POST(RestApiConstants.URL_POST_FAV_BY_USER)
    Call<Base<Journey>> setFavoriteJourneyByUser(@Path("user") int userId, @Body Journey journey);

    @DELETE(RestApiConstants.URL_DELETE_FAV_BY_USER)
    Call<Base<Clothe>> removeFavoriteByUserByClothe(@Path("user") int userId, @Path("idType") int idClothe, @Query("type") String type);

    @DELETE(RestApiConstants.URL_DELETE_FAV_BY_USER)
    Call<Base<Trend>> removeFavoriteByUserByTrend(@Path("user") int userId, @Path("idType") int idTrend, @Query("type") String type);

    @DELETE(RestApiConstants.URL_DELETE_FAV_BY_USER)
    Call<Base<Tip>> removeFavoriteByUserByTip(@Path("user") int userId, @Path("idType") int idTip, @Query("type") String type);

    @DELETE(RestApiConstants.URL_DELETE_FAV_BY_USER)
    Call<Base<WayDressing>> removeFavoriteByUserByWayDress(@Path("user") int userId, @Path("idType") int idTip, @Query("type") String type);

    @DELETE(RestApiConstants.URL_DELETE_FAV_BY_USER)
    Call<Base<Journey>> removeFavoriteByUserByJourney(@Path("user") int userId, @Path("idType") int idJourney, @Query("type") String type);

    @GET(RestApiConstants.URL_GET_FAVORITES_BY_USER)
    Call<Base<Favorite>> getFavoritesByUser(@Path("user") int userId);

    @GET(RestApiConstants.URL_GET_JOURNEYS)
    Call<Base<ArrayList<Journey>>> getJourneys();

    @GET(RestApiConstants.URL_GET_JOURNEY_BY_ID)
    Call<Base<Journey>> getJourneyById(@Path("journey") int idJourney);

    /**
     * Almacena en el servidor fme la imagen de chat
     * @param key La key (uid) de firebase
     * @param image Imagen a subir
     * @param idUser Id de usuario
     * @return Respuesta de peticion
     */
    @Multipart
    @POST(RestApiConstants.URL_POST_IMAGE_CHAT)
    Call<Base<String>> uploadImageChat(@Part("key") RequestBody key, @Part MultipartBody.Part image, @Path("user") int idUser);

    /**
     * Obtiene el UID del chat assessor-client
     * @param userId El id del usuario
     * @return Respuesta de servidor
     */
    @GET(RestApiConstants.URL_GET_UID_CHAT)
    Call<Base<ChatAssessorClient>> getUIDByUserId(@Path("user") int userId);

    /**
     * Registra el uid de firebase para el chat
     * @param userId Id del usuario
     * @param key Key uid de firebase relacionada con el chat
     * @return
     */
    @POST(RestApiConstants.URL_POST_REGISTERUID_CHAT)
    Call<Base<ChatAssessorClient>> registerUID(@Path("user") int userId, @Body ChatClient key);

    /**
     * Registra un token de dispositivo
     * @return
     */
    @POST(RestApiConstants.URL_POST_REGISTER_TOKEN)
    Call<Base<DeviceToken>> registerDeviceToken(@Body DeviceToken token);

    /**
     * Actualiza el token asociandolo al usuario
     * @param tokenId Id del token
     * @param deviceToken Objeto con los datos a actualizar
     * @return
     */
    @PUT(RestApiConstants.URL_PUT_TOKENS)
    Call<Base<DeviceToken>> updateToken(@Path("token") int tokenId, @Body DeviceToken deviceToken);

    /**
     * Actualiza el token asociado al usuario especificado
     * @param userId Id del usuario
     * @param deviceToken Cuerpo de la peticion con el token
     * @return Un objeto de DeviceToken
     */
    @POST(RestApiConstants.URL_POST_REGISTER_TOKEN_BY_USER)
    Call<Base<DeviceToken>> updateTokenByUser(@Path("user") int userId, @Body DeviceToken deviceToken);

    /**
     * Obtiene la lista de chats
     * @return Arraylist con los chats registrados
     */
    @GET(RestApiConstants.URL_GET_CHATS)
    Call<Base<ArrayList<ChatAssessorClient>>> getChats(@Query("assessor") String assessor);

    /**
     * Metodo que actualiza el chat para establecer un asesor
     * @param newChat Objeto con los parametros
     * @param id Id del chat
     * @return Objeto chat
     */
    @PUT(RestApiConstants.URL_PUT_CHAT_ASSESSOR)
    Call<Base<ChatAssessorClient>> updateChat(@Body ChatAssessorClient newChat, @Path("chat") int id);

    /**
     * Registra el tipo de cara relacionada con el usuario
     * @param faceType Cuerpo de la peticion con el tipo de cara
     * @param userId Path con el id del usuario
     * @return Objeto User
     */
    @POST(RestApiConstants.URL_POST_USER_FACE)
    Call<Base<User>> registerFace(@Body FaceType faceType, @Path("user") int userId);

    /**
     * Obtiene listado de visajismos relacionados con el usuario
     * @param userId
     * @return
     */
    @GET(RestApiConstants.URL_GET_VISAGES_BY_USER)
    Call<Base<ArrayList<Visage>>> getVisageByUser(@Path("user") int userId, @Query("genre") String genre);

    /**
     * Obtiene el listado de makups relacionados con el usuario
     * @param userId Id del usuario
     * @return Lista con los objetos makeup
     */
    @GET(RestApiConstants.URL_GET_MAKEUPS_BY_USER)
    Call<Base<ArrayList<Makeup>>> getMakeupsByUser(@Path("user") int userId);

    /**
     * Obtiene el listado de cabello relacionado con el usuario
     * @param userId Id del usaurio
     * @return
     */
    @GET(RestApiConstants.URL_GET_HAIR_BY_USER)
    Call<Base<ArrayList<Makeup>>> getHairByUser(@Path("user") int userId);

    /**
     * Obtiene usuarios filtrado por email
     * @param email Email del usuario
     * @return Arreglo de usuarios
     */
    @GET(RestApiConstants.URL_GET_USERS)
    Call<Base<ArrayList<User>>> getUserByEmail(@Query("email") String email);

    @PUT(RestApiConstants.URL_UPDATE_PREMIUM)
    Call<Base<User>> updatePremium(@Body Premium premium, @Path("user") int userId);

    /**
     * Recuperar password
     */
    @POST(RestApiConstants.URL_RECOVER_PASSWORD)
    Call<Forgot> recoverPassword(@Body Forgot email);

    @GET(RestApiConstants.URL_GET_QUESTIONS_COLOR)
    Call<Base<ArrayList<QuestionColor>>> getQuestionsColor();

    /**
     * Listado de codigos de vestimenta
     * @return Data con codigos de vestimenta
     */
    @GET(RestApiConstants.URL_GET_DRESS_CODES)
    Call<Base<ArrayList<DressCode>>> getDressCodes();

    /**
     * Listado de climas
     * @return Data con los climas
     */
    @GET(RestApiConstants.URL_GET_CLIMATES)
    Call<Base<ArrayList<Climate>>> getClimates();

    /**
     * Listado de categorias
     * @return Data con las subcategorias
     */
    @GET(RestApiConstants.URL_GET_SUBCATEGORIES_ALL)
    Call<Base<ArrayList<Subcategory>>> getSubcategoriesAll();

    /**
     * Listado de colores
     * @return
     */
    @GET(RestApiConstants.URL_GET_COLORS)
    Call<Base<ArrayList<Colorimetry>>> getColors();

    /**
     * Registra una imagen de ropa en el closet personal del usuario
     * @return Data con la ropa registrada
     */
    @Multipart
    @POST(RestApiConstants.URL_REGISTER_CLOTHE_BY_USER)
    Call<Base<Clothe>> registerClotheByUser(
            @Part("name") RequestBody name,
            @Part MultipartBody.Part image,
            @Part("dressCodes[]") ArrayList<Integer> idsDress,
            @Part("climates[]") ArrayList<Integer> idsClimate,
            @Part("subcategories[]") ArrayList<Integer> idsSubcategory,
            @Part("colors[]") ArrayList<Integer> idsColor,
            @Path("user") int userId);

    /**
     * Obtiene listado de ropa registrada por el usuario en su closet personal
     * @param userId Id del usuario
     * @return Data con la ropa del usuario
     */
    @GET(RestApiConstants.URL_GET_PERSONAL_CLOTHES_BY_USER)
    Call<Base<ArrayList<Clothe>>> getClothesByUser(@Path("user") int userId);

    /**
     * Obtiene recomendaciones de ropa de closet personal
     * @param userId Id del usuario
     * @param options Parametro de clima
     * @return Data con la ropa recomendada
     */
    @GET(RestApiConstants.URL_GET_CLOSET_PERSONAL_RECOMMENDATIONS)
    Call<Base<ArrayList<Clothe>>> getRecommendationsByUserClothes(@Path("user") int userId, @QueryMap Map<String, String> options);
}

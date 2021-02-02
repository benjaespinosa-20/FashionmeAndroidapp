package mx.app.fashionme.db;

/**
 * Created by heriberto on 13/03/18.
 */

public final class DataBaseConstants {

    public static final String DATABASE_NAME                        = "fashion_me";
    public static final int DATABASE_VERSION                        = 7;


    public static final String TABLE_LOOK                           = "look";
    public static final String TABLE_LOOK_ID                        = "id";
    public static final String TABLE_LOOK_URI                       = "uri";
    public static final String TABLE_LOOK_DATE                      = "date";

    public static final String TABLE_FAVS                           = "favs";
    public static final String TABLE_FAVS_ID                        = "id";
    public static final String TABLE_FAVS_ID_CLOTHE                 = "id_clothe";
    public static final String TABLE_FAVS_NAME_CLOTHE               = "name_clothe";
    public static final String TABLE_FAVS_IMAGE_CLOTHE              = "image_clothe";

    public static final String TABLE_SHOPPING                       = "cart";
    public static final String TABLE_SHOPPING_ID                    = "id";
    public static final String TABLE_SHOPPING_ID_CLOTHE             = "id_clothe";
    public static final String TABLE_SHOPPING_NAME_CLOTHE           = "name_clothe";
    public static final String TABLE_SHOPPING_NAME_CLOTHE_ENGLISH   = "name_clothe_english";
    public static final String TABLE_SHOPPING_URL_CLOTHE            = "url_clothe";
    public static final String TABLE_SHOPPING_IMAGE_CLOTHE          = "image_clothe";

    public static final String TABLE_CHECKLIST                      = "checklist";
    public static final String TABLE_CHECKLIST_ID                   = "id";
    public static final String TABLE_CHECKLIST_ID_JOURNEY           = "id_journey";
    public static final String TABLE_CHECKLIST_ID_ITEM              = "id_item";

}

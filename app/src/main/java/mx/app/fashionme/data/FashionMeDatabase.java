package mx.app.fashionme.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import mx.app.fashionme.data.database.daos.ChatDao;
import mx.app.fashionme.data.database.entities.ChatEntity;

/**
 * FashionMeDatabase
 *
 * Construccion de base de datos
 */
@Database(
        entities = {
                ChatEntity.class
        },
        version = 1, exportSchema = false
)
public abstract class FashionMeDatabase extends RoomDatabase {

    /**
     * Nombre de la base de datos
     */
    public static final String DB_NAME = "fashion-me-db";

    /**
     * Metodo para obtener un dao de chat
     *
     * @return Chat dao
     */
    public abstract ChatDao chatDao();

}

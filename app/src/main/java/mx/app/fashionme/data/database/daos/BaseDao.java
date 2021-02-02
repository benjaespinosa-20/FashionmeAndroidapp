package mx.app.fashionme.data.database.daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Transaction;
import androidx.room.Update;

/**
 * BaseDao
 * Contiene los metodos base insert, update, delete
 *
 * @author heriberto martinez elizarraraz
 * @since 01-octubre-2020
 */
public interface BaseDao<T> {

    /**
     * Inserta un objeto en la base de datos
     *
     * @param obj objeto a insertar en la base de datos
     *
     * @return columnas actualizadas en bd
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T obj);

    /**
     * Actualiza un objeto en la base de datos
     *
     * @param obj objeto a actualizar en la base de datos
     *
     * @return columnas actualizadas
     */
    @Update
    int update(T obj);

    /**
     * Borra un objeto de la base de datos
     *
     * @param obj objeto a borrar
     *
     * @return columnas actualizadas
     */
    @Delete
    int delete(T obj);
}

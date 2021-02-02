package mx.app.fashionme.data.database.daos;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import mx.app.fashionme.data.database.entities.ChatEntity;

/**
 * Clase abstracta para definir acciones en la entidad de ChatEntity
 * y llamadas a la base de datos
 *
 * @author heriberto martinez elizarraraz
 * @since 01-octubre-2020
 */
@Dao
public interface ChatDao extends BaseDao<ChatEntity> {

    /**
     * Metodo para obtener el tiempo de chat restante
     *
     * @return tiempo restante
     */
    @Query("SELECT remainingTime FROM assessor_chat WHERE id = 1")
    long getRemainingTime();

    @Query("SELECT * FROM assessor_chat")
    List<ChatEntity> loadAllValues();

    /**
     * Metodo para actualizar la fecha de ultima entrada al chat
     *
     * @param lastEntryTime hora de ultima entrada
     *
     * @return columnas actualizadas
     */
    @Query("UPDATE assessor_chat SET lastEntryChat = :lastEntryTime WHERE id = 1")
    int updateLastEntry(long lastEntryTime);

    /**
     * Metodo para actualizar tiempo restante
     *
     * @param time tiempo restante
     *
     * @return columnas actualizadas
     */
    @Query("UPDATE assessor_chat SET remainingTime = :time WHERE id = 1")
    int updateTimeRemaining(long time);

}

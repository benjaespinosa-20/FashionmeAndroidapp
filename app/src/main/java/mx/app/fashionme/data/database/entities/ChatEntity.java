package mx.app.fashionme.data.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Clase entidad
 * Tabla de base de datos
 *
 */
@Entity(tableName = "assessor_chat")
public class ChatEntity {
    @PrimaryKey(autoGenerate = false)
    public int id;
    public long remainingTime;
    public long boughtTimeInMillis;
    public long lastEntryChat;
}

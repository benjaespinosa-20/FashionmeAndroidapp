package mx.app.fashionme.data.repositories;

import mx.app.fashionme.data.database.daos.ChatDao;

/**
 * DataBaseRepository
 * Repositorio de base de datos que provee metodos de instancias de dao's
 *
 * @author heriberto martinez elizarraraz
 * @since 01-octubre-2020
 */
public interface DataBaseRepository {

    /**
     * Metodo para obtener una instancia de chat dao
     *
     * @return Instancia de ChatDao
     */
    ChatDao chatDao();
}

package mx.app.fashionme.data.repositories.impl;

import javax.inject.Inject;

import mx.app.fashionme.data.database.daos.ChatDao;
import mx.app.fashionme.data.repositories.DataBaseRepository;

/**
 * DataBaseRepositoryImpl
 * Clase que implementa el repositorio de base de datos
 * donde se implementan los metodos de instancias de dao's
 *
 * @author heriberto martinez elizarraraz
 * @since 01-octubre-2020
 */
public class DataBaseRepositoryImpl implements DataBaseRepository {

    /**
     * Instancia de chat dao
     */
    private ChatDao chatDao;

    /**
     * Metodo construnctor que provee una instancia de chat dao
     * @param chatDao instnacia chat dao
     */
    @Inject
    public DataBaseRepositoryImpl(ChatDao chatDao) {
        this.chatDao = chatDao;
    }

    /**
     * Metodo para obtener una instancia de chat dao
     *
     * @return Instancia de ChatDao
     */
    @Override
    public ChatDao chatDao() {
        return chatDao;
    }

}

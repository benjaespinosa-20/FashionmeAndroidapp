package mx.app.fashionme.di.modules.data;

import dagger.Module;
import dagger.Provides;
import mx.app.fashionme.data.database.daos.ChatDao;
import mx.app.fashionme.data.repositories.DataBaseRepository;
import mx.app.fashionme.data.repositories.impl.DataBaseRepositoryImpl;

/**
 * RepositoryModule
 */
@Module(includes = {DataHelpersModule.class, NetworkModule.class})
public class RepositoryModule {

    /**
     * Metodo que provee una instancia de repositorio de base de datos
     *
     * @param  chatDao instancia de chatDao
     *
     * @return retorna una instancia de repositorio bd
     */
    @Provides
    DataBaseRepository provideDataBaseRepository(ChatDao chatDao) {
        return new DataBaseRepositoryImpl(chatDao);
    }
}

package mx.app.fashionme.di.modules.data;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mx.app.fashionme.data.FashionMeDatabase;
import mx.app.fashionme.data.database.daos.ChatDao;

/**
 * Modulo para proveer instancias
 */
@Module
public class DatabaseModule {

    @Singleton
    @Provides
    FashionMeDatabase provideRoomDatabase(Context context) {
        return Room
                .databaseBuilder(context, FashionMeDatabase.class, FashionMeDatabase.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }


    /**
     * Provee una instancia de chat dao
     * @param fashionMeDatabase instancia de fashion me database
     * @return Instancia ChatDao
     */
    @Provides
    ChatDao provideChatDao(FashionMeDatabase fashionMeDatabase) {
        return fashionMeDatabase.chatDao();
    }

}

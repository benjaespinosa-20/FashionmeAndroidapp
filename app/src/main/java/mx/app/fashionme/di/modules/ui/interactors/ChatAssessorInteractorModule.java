package mx.app.fashionme.di.modules.ui.interactors;

import dagger.Module;
import dagger.Provides;
import mx.app.fashionme.data.repositories.DataBaseRepository;
import mx.app.fashionme.interactor.ChatAssessorInteractor;
import mx.app.fashionme.interactor.interfaces.IChatAssessorInteractor;

/**
 * ChatAssesorInteractorModule
 * clase para inyectar dependencias del modulo de chat
 */
@Module
public class ChatAssessorInteractorModule {

    /**
     * Provee una instancia de chat assessor interactor
     *
     * @param dataBaseRepository instancia de database repo
     *
     * @return una instancia de ChatAssessorInteractor
     */
    @Provides
    IChatAssessorInteractor provideChatAssessorInteractor(DataBaseRepository dataBaseRepository) {
        return new ChatAssessorInteractor(dataBaseRepository);
    }

}

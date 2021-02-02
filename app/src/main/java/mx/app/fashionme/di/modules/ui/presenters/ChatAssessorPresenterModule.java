package mx.app.fashionme.di.modules.ui.presenters;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import mx.app.fashionme.interactor.interfaces.IChatAssessorInteractor;
import mx.app.fashionme.presenter.ChatAssessorPresenter;
import mx.app.fashionme.presenter.interfaces.IChatAssessorPresenter;

/**
 * ChatAssessorPresenterModule
 */
@Module
public class ChatAssessorPresenterModule {

    /**
     * Metodod para proveer una instancia de chat assessor presenter
     *
     * @param chatAssessorInteractor instancia de chat assessor interactor
     * @param application instancia de application
     *
     * @return ChatAssessorPresenter
     */
    @Provides
    IChatAssessorPresenter provideChatAssessorPresenter(IChatAssessorInteractor chatAssessorInteractor,
                                                       Application application) {
        return new ChatAssessorPresenter(chatAssessorInteractor, application);
    }

}

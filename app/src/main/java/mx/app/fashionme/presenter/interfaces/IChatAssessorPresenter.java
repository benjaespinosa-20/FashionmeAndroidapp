package mx.app.fashionme.presenter.interfaces;

import android.net.Uri;
import android.view.Menu;
import android.view.View;

import com.google.firebase.database.DatabaseReference;

import mx.app.fashionme.pojo.FashionMessage;
import mx.app.fashionme.ui.modules.commons.BaseContract;
import mx.app.fashionme.view.interfaces.IChatAssessorView;

public interface IChatAssessorPresenter extends BaseContract.IPresenterBase<IChatAssessorView> {

    void initializeChat();

    void instantiateFirebase();

    void addFirebaseListener(DatabaseReference firebaseInstance, String key);

    void sendMessage(FashionMessage message);

    String getUsername();

    String getCurrentDate();

    String getEmailUser();

    void startAdapterListening();

    void stopAdapterListening();

    void createAdapter();

    void showMenu(Menu menu);

    void sendImageMessage(FashionMessage tempMessage, Uri uri);

    void setAnalytics();

    void setCount();

    void registerChat(DatabaseReference firebaseInstance);

    /**
     * Metodo para guardar nuevo tiempo restante
     * @param timeUntilFinished tiempo restante hasta que termine
     */
    void saveTimeRemaining(long timeUntilFinished);
}

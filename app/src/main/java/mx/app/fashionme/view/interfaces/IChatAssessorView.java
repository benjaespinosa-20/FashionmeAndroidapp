package mx.app.fashionme.view.interfaces;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import mx.app.fashionme.adapter.ChatFirebaseAdapter;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.pojo.FashionMessage;
import mx.app.fashionme.ui.modules.commons.BaseContract;

public interface IChatAssessorView extends BaseContract.IViewBase {
    void registerNewChat();
    
    void showProgressBar();

    void hideProgressBar();

    void showLinearLayoutChat();

    void hideLinearLayoutChat();

    DatabaseReference getFirebaseInstance();

    void setFirebaseInstance(DatabaseReference firebaseInstance);

    String getKeyUID();

    void setKeyUID(String keyUID);

    void showMessageNotAssessor();

    void hideSnackbar();

    ChatAssessorClient getChat();

    void setChat(ChatAssessorClient chat);

    ChatFirebaseAdapter createAdapter(FirebaseRecyclerOptions<FashionMessage> options);

    void initializeAdapter();

    void generateLayout();

    void startAdapter();

    void stopAdapter();

    ChatFirebaseAdapter getAdapter();

    void countVisible();

    /**
     * Metodo para mostrar contador
     *
     * @param remainingTime tiempo restante
     */
    void setCount(long remainingTime);
}

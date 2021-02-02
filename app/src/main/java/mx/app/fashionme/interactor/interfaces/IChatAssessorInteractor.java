package mx.app.fashionme.interactor.interfaces;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;

import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.pojo.FashionMessage;

public interface IChatAssessorInteractor {

    void init(IChatAssessorInteractor.ChatCallback listener);

    void getUID(Context context, ChatCallback callback);

    DatabaseReference getInstanceFirebase();

    void pushChat(Context context, DatabaseReference firebaseInstance, ChatCallback callback);

    void registerUIDF(Context context, String key, ChatCallback callback);

    FirebaseRecyclerOptions<FashionMessage> getOptions(DatabaseReference messageRef, SnapshotParser<FashionMessage> parser);

    SnapshotParser<FashionMessage> getParser();

    void sendMessageToFirebase(DatabaseReference firebaseInstance, String keyUid, FashionMessage message);

    void sendImageMessageToFirebase(DatabaseReference firebaseInstance, String keyUID, Uri uri, FashionMessage tempMessage, Context context);

    /**
     * Metodo para obtener el tiempo restante
     */
    void getRemainingTime();

    /**
     * Metodo para guardar nuevo tiempo restante
     * @param timeUntilFinished tiempo restante hasta que termine
     */
    void saveTimeRemaining(long timeUntilFinished);

    interface ChatCallback {
        void onGetUID(ChatAssessorClient data);
        void onRegisterUID(ChatAssessorClient data);

        void onErrorRequest(String error);

        void onCompleteRegister(String key);

        /**
         * Metodo de respuesta al obtener el tiempo restante
         *
         * @param remainingTime tiempo restante
         */
        void onGettingTime(long remainingTime);
    }
}

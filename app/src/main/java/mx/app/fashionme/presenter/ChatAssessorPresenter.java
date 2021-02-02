package mx.app.fashionme.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IChatAssessorInteractor;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.pojo.FashionMessage;
import mx.app.fashionme.pojo.firebase.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.interfaces.IChatAssessorPresenter;
import mx.app.fashionme.ui.modules.commons.BasePresenter;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.view.ClientProfileActivity;
import mx.app.fashionme.view.interfaces.IChatAssessorView;

import static mx.app.fashionme.utils.Constants.CLIENT_EMAIL;

public class ChatAssessorPresenter extends BasePresenter<IChatAssessorView> implements IChatAssessorPresenter, IChatAssessorInteractor.ChatCallback {

    private IChatAssessorInteractor interactor;
    private Context context;


    @Inject
    public ChatAssessorPresenter(IChatAssessorInteractor interactor, Context context) {
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void initializeChat() {
        if (mView != null) {
            mView.showProgressBar();
            mView.hideLinearLayoutChat();

            if (mView.getChat() == null){
                interactor.getUID(context, this);
            } else {
                onGetUID(mView.getChat());
            }
        }
    }

    @Override
    public void instantiateFirebase() {
        if (mView != null){
            mView.setFirebaseInstance(interactor.getInstanceFirebase());
        }
    }

    @Override
    public void addFirebaseListener(DatabaseReference firebaseInstance, String key) {
        if (firebaseInstance != null){
            firebaseInstance.child("chats").child(key).child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null){
                        if (user.getAssessor() != null && mView != null){
                            mView.hideProgressBar();
                            mView.hideSnackbar();
                            mView.showLinearLayoutChat();
                            startAdapterListening();
                        } else if (user.getAssessor() == null && mView != null) {
                            mView.showMessageNotAssessor();
                            mView.showProgressBar();
                            mView.hideLinearLayoutChat();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void sendMessage(FashionMessage message) {
        interactor.sendMessageToFirebase(mView.getFirebaseInstance(), mView.getKeyUID(), message);
    }

    @Override
    public String getUsername() {
        return SessionPrefs.get(context).getUserName();
    }

    @Override
    public String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return format.format(date);
    }

    @Override
    public String getEmailUser() {
        return SessionPrefs.get(context).getEmail();
    }

    @Override
    public void startAdapterListening() {
        mView.startAdapter();
    }

    @Override
    public void stopAdapterListening() {
        mView.stopAdapter();
    }

    @Override
    public void createAdapter() {
        if (mView != null) {
            mView.createAdapter(
                    interactor.getOptions(
                            mView.getFirebaseInstance().getRef().child("chats").child(mView.getKeyUID()).child("messages"),
                            interactor.getParser()
                    ));

            mView.generateLayout();

            mView.initializeAdapter();
        }
    }

    @Override
    public void showMenu(Menu menu) {
        if (mView != null) {
            if (SessionPrefs.get(context).getRole() != null && !SessionPrefs.get(context).getRole().equals("assessor")){
                menu.removeItem(R.id.menu_chat_assessor_show_contact);
            }
        }
    }

    @Override
    public void sendImageMessage(FashionMessage tempMessage, Uri uri) {
        interactor.sendImageMessageToFirebase(mView.getFirebaseInstance(), mView.getKeyUID(), uri, tempMessage, context);
    }

    @Override
    public void setAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Chat asesor/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    @Override
    public void setCount() {
        if (SessionPrefs.get(context).getRole().equals("assesor")) {
            mView.countVisible();
        } else {
            interactor.getRemainingTime();
        }
    }

    @Override
    public void onGetUID(ChatAssessorClient data) {
        if (mView != null) {
            if (data == null) {
                mView.registerNewChat();
            } else {
                if (data.getAssessor() == null){
                    mView.showMessageNotAssessor();
                }
                mView.setKeyUID(data.getFuid());

                if (mView.getAdapter() == null) {
                    createAdapter();
                }

            }
        }
    }

    @Override
    public void onRegisterUID(ChatAssessorClient data) {
        if(mView != null) {
            mView.setKeyUID(data.getFuid());
            if (data.getAssessor() == null) {
                mView.showMessageNotAssessor();
            } else {
                mView.hideProgressBar();
                mView.showLinearLayoutChat();
            }
        }
    }

    @Override
    public void onErrorRequest(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleteRegister(String key) {
        if (mView != null) {
            mView.setKeyUID(key);
        }
        createAdapter();
        interactor.registerUIDF(context, key, this);
    }

    /**
     * Metodo de respuesta al obtener el tiempo restante
     *
     * @param remainingTime tiempo restante
     */
    @Override
    public void onGettingTime(long remainingTime) {
        if (mView != null) {
            mView.setCount(remainingTime);
        }
    }

    @Override
    public void registerChat(DatabaseReference firebaseInstance) {
        interactor.pushChat(context, firebaseInstance, this);
    }

    /**
     * Metodo para guardar nuevo tiempo restante
     * @param timeUntilFinished tiempo restante hasta que termine
     */
    @Override
    public void saveTimeRemaining(long timeUntilFinished) {
        interactor.saveTimeRemaining(timeUntilFinished);
    }

    @Override
    protected void init() {
        interactor.init(this);
    }

}

package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IChatsInteractor;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.presenter.interfaces.IChatsPresenter;
import mx.app.fashionme.view.ChatAssessorActivity;
import mx.app.fashionme.view.interfaces.IChatsView;

public class ChatsPresenter implements IChatsPresenter, IChatsInteractor.ChatsListener {

    private static final String TAG = "Chats";
    public static final String KEY_FROM_ASSESSOR = "chat";

    private Activity activity;
    private Context context;
    private IChatsView chatsView;
    private IChatsInteractor chatsInteractor;

    public ChatsPresenter(Activity activity, Context context,
                          IChatsView chatsView, IChatsInteractor chatsInteractor) {
        this.activity           = activity;
        this.context            = context;
        this.chatsView          = chatsView;
        this.chatsInteractor    = chatsInteractor;
    }

    @Override
    public void getPendingChats() {
        if (chatsView != null) {
            chatsView.showProgress();
            if (!isOnline()){
                chatsView.hideProgress();
                chatsView.showLinearLayoutError();
                chatsView.showOffline();
            } else {
                chatsView.hideLinearLayoutError();
                chatsView.showList();
                chatsInteractor.getData(context, this);
            }
        }

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void acceptChat(ChatAssessorClient chatAssessorClient) {
        chatsView.showProgress();
        chatsInteractor.updateChat(context, chatAssessorClient, this);
    }

    @Override
    public void onGetData(ArrayList<ChatAssessorClient> data) {
        if (chatsView != null) {
            chatsView.hideProgress();
            chatsView.hideLinearLayoutError();
            if (data.size() == 0) {
                chatsView.showLinearLayoutError();
                chatsView.emptyList();
            }
            chatsView.generateLinearLayout();
            chatsView.initializeAdapter(chatsView.createAdapter(), data);
        }
    }

    @Override
    public void onErrorGetData(String error) {
        if (chatsView != null) {
            chatsView.hideProgress();
            chatsView.hideList();
            chatsView.showLinearLayoutError();
            chatsView.showError(error);
        }
    }

    @Override
    public void onErrorUpdateChat(String error) {
        if (chatsView != null) {
            chatsView.hideProgress();
            chatsView.hideList();
            chatsView.showLinearLayoutError();
            chatsView.showError(error);
        }
    }

    @Override
    public void onSuccessUpdateChat(ChatAssessorClient data) {
        Intent intent = new Intent(context, ChatAssessorActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_FROM_ASSESSOR, data);
        context.startActivity(intent);
    }
}

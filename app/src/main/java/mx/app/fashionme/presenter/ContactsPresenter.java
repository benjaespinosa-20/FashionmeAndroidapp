package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

import mx.app.fashionme.interactor.interfaces.IContactsInteractor;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.presenter.interfaces.IContactsPresenter;
import mx.app.fashionme.view.ChatAssessorActivity;
import mx.app.fashionme.view.interfaces.IContactsView;

public class ContactsPresenter implements IContactsPresenter, IContactsInteractor.ContactsListener {

    private Activity activity;
    private Context context;
    private IContactsView view;
    private IContactsInteractor interactor;

    public ContactsPresenter(Activity activity, Context context,
                             IContactsView view, IContactsInteractor interactor) {
        this.activity   = activity;
        this.context    = context;
        this.view       = view;
        this.interactor = interactor;
    }

    @Override
    public void getContacts() {
        if (view != null) {
            view.showProgress();
            if (!isOnline()){
                view.hideProgress();
                view.showLinearLayoutError();
                view.showOffline();
            } else {
                view.hideLinearLayoutError();
                view.showList();
                interactor.getData(context, this);
            }
        }
    }

    @Override
    public void openChat(ChatAssessorClient chatAssessorClient) {
        Intent intent = new Intent(context, ChatAssessorActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ChatsPresenter.KEY_FROM_ASSESSOR, chatAssessorClient);
        context.startActivity(intent);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public void onGetData(ArrayList<ChatAssessorClient> data) {
        if (view != null) {
            view.hideProgress();
            view.hideLinearLayoutError();
            if (data.size() == 0) {
                view.showLinearLayoutError();
                view.emptyList();
            }
            view.generateLinearLayout();
            view.initializeAdapter(view.createAdapter(), data);
        }
    }

    @Override
    public void onErrorGetData(String error) {
        if (view != null){
            view.hideProgress();
            view.hideList();
            view.showLinearLayoutError();
            view.showError(error);
        }
    }
}

package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.ICharacteristicInteractor;
import mx.app.fashionme.pojo.Characteristic;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.interfaces.ICharacteristicPresenter;
import mx.app.fashionme.view.ColorTestActivity;
import mx.app.fashionme.view.interfaces.ICharacteristicView;

/**
 * Created by desarrollo1 on 20/03/18.
 */

public class CharacteristicPresenter implements ICharacteristicPresenter, ICharacteristicInteractor.OnGetData, ICharacteristicInteractor.OnSendDataFinishedListener {
    private ICharacteristicView view;
    private ICharacteristicInteractor interactor;
    private Context context;
    private Activity activity;

    public CharacteristicPresenter(ICharacteristicView view, ICharacteristicInteractor interactor, Context context, Activity activity) {
        this.view       = view;
        this.interactor = interactor;
        this.context    = context;
        this.activity   = activity;
    }


    @Override
    public void onError(String error) {
        view.getError(error);
    }

    @Override
    public void onSuccess(ArrayList<Characteristic> characteristics) {
        view.setAdapterCharacteristic(characteristics);
    }

    @Override
    public void getData() {
//        String body = SessionPrefs.get(context).getBodyType(context.getString(R.string.app_language));
//        if (body == null) {
//            view.alertBody();
//            return;
//        }
        interactor.getDataFromApi(this);
    }

    @Override
    public void sendData(ListView listView) {
        interactor.sendToServer(listView, context, this);
    }


    @Override
    public void onErrorSendData(String error) {
        view.getError(error);
    }

    @Override
    public void onSuccess() {
        activity.startActivity(new Intent(activity, ColorTestActivity.class));
        activity.finish();
    }
}

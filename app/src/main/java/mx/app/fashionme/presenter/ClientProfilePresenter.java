package mx.app.fashionme.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.interfaces.IClientProfileInteractor;
import mx.app.fashionme.pojo.User;
import mx.app.fashionme.presenter.interfaces.IClientProfilePresenter;
import mx.app.fashionme.view.interfaces.IClientProfileView;

public class ClientProfilePresenter implements IClientProfilePresenter, IClientProfileInteractor.ClientProfileListener {

    private static final String TAG = "ClientProfilePres";
    private IClientProfileView view;
    private Activity activity;
    private Context context;
    private IClientProfileInteractor interactor;

    public ClientProfilePresenter(IClientProfileView view, Activity activity,
                                  Context context, IClientProfileInteractor interactor) {
        this.view       = view;
        this.activity   = activity;
        this.context    = context;
        this.interactor = interactor;
    }

    @Override
    public void getProfileUser(String clientEmail) {
        if (view != null){
            view.showProgress();
            view.hideLayoutProfile();
            if (clientEmail == null){
                view.showToast(activity.getString(R.string.not_profile_found));
                view.notEmail();
                return;
            }
        }
        interactor.getUser(context, clientEmail, this);
    }

    @Override
    public void onErrorGetData(String error) {
        if (view != null) {
            view.hideProgress();
            view.showToast(error);
        }
    }

    @Override
    public void onGetUser(User user) {
        if (view != null) {
            view.hideProgress();
            view.showLayoutProfile();

            view.setName(user.getName());
            view.setEmail(user.getEmail());

            String genre = user.getGenre();

            switch (context.getResources().getString(R.string.app_language)) {
                case "spanish":
                    if (genre.equals("woman")){
                        view.setGenre("Mujer");
                    } else if (genre.equals("man")){
                        view.setGenre("Hombre");
                    }

                    view.setStyle(user.getStyle() != null ? user.getStyle().getData().getSpanish().getName():activity.getString(R.string.no_register));
                    view.setBodyType(user.getBody() != null ? user.getBody().getData().getSpanish().getBody_type():activity.getString(R.string.no_register));
                    view.setColorName(user.getColor() != null ? user.getColor().getData().getSpanish().getColor_name():activity.getString(R.string.no_register));
                    break;
                case "english":
                    view.setGenre(genre);
                    view.setStyle(user.getStyle() != null ? user.getStyle().getData().getEnglish().getName():activity.getString(R.string.no_register));
                    view.setBodyType(user.getBody() != null ? user.getBody().getData().getEnglish().getBody_type():activity.getString(R.string.no_register));
                    view.setColorName(user.getColor() != null ? user.getColor().getData().getEnglish().getColor_name():activity.getString(R.string.no_register));
                    break;
                    default:
                        view.setStyle(user.getStyle() != null ? user.getStyle().getData().getSpanish().getName():activity.getString(R.string.no_register));
                        view.setBodyType(user.getBody() != null ? user.getBody().getData().getSpanish().getBody_type():activity.getString(R.string.no_register));
                        view.setColorName(user.getColor() != null ? user.getColor().getData().getSpanish().getColor_name():activity.getString(R.string.no_register));
                        break;
            }

            if (user.getColor() != null) {
                view.setImagePalette(user.getColor().getData().getUrlImage());
            }
        }
    }
}

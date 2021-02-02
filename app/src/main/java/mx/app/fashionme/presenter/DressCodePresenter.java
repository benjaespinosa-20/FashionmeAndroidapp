package mx.app.fashionme.presenter;

import android.content.Context;
import android.widget.Toast;

import mx.app.fashionme.interactor.interfaces.IDressCodeInteractor;
import mx.app.fashionme.pojo.DressCode;
import mx.app.fashionme.pojo.WayDressing;
import mx.app.fashionme.presenter.interfaces.IDressCodePresenter;
import mx.app.fashionme.view.interfaces.IDressCodeView;

public class DressCodePresenter implements IDressCodePresenter, IDressCodeInteractor.OnGetDressCodeFinishedListener, IDressCodeInteractor.DressCodeListener {

    public static final String TAG = DressCodePresenter.class.getSimpleName();
    private IDressCodeView view;
    private IDressCodeInteractor interactor;
    private Context context;

    public DressCodePresenter(IDressCodeView view, IDressCodeInteractor interactor, Context context) {
        this.view = view;
        this.interactor = interactor;
        this.context = context;
    }

    @Override
    public void getDataDressCode(int idDressCode) {
        view.showLoading(true);
        view.showEmpty(false);
        interactor.getDressCode(context, idDressCode,this);
        checkFav(idDressCode);
    }

    @Override
    public void checkFav(int idDressCode) {
        interactor.checkFavDressCode(context, idDressCode, this);
    }

    @Override
    public void removeFavWay(int idDressCode) {
        interactor.removeFavByIdDressCode(context, idDressCode, this);
    }

    @Override
    public void addFavWay(int idDressCode) {
        interactor.addToFav(context, idDressCode, this);
    }

    @Override
    public void onSuccess(WayDressing wayDressing) {
        if (view != null) {

            view.showLoading(false);

            if (wayDressing == null) {
                view.showEmpty(true);
                return;
            }

            view.generateLinearLayoutDressCode();
            view.initializeAdapter(view.createAdapter(wayDressing));
        }
    }

    @Override
    public void onFail(String err) {
        if (view != null) {
            view.showLoading(false);
            view.showError(err);
        }
    }

    @Override
    public void onCheckFav(boolean isFav) {
        if (view != null) {
            view.setButtonFav(isFav);
        }
    }

    @Override
    public void onAddTrendFav() {
        if (view != null) {
            view.favAdded();
            Toast.makeText(context, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveTrendFav() {
        if (view != null) {
            view.favRemoved();
            Toast.makeText(context, "Removido de favoritos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String error) {
        if (view != null) {
            view.showError(error);
        }
    }
}

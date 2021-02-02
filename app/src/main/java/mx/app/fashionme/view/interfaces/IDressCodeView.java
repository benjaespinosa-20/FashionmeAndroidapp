package mx.app.fashionme.view.interfaces;

import mx.app.fashionme.adapter.DressCodeAdapter;
import mx.app.fashionme.pojo.WayDressing;

public interface IDressCodeView {

    void setUpPresenterDressCode();
    void getDataDressCode();
    void setToolbarDressCode();
    void showLoading(boolean show);
    void showEmpty(boolean show);
    void generateLinearLayoutDressCode();
    DressCodeAdapter createAdapter(WayDressing wayDressing);
    void initializeAdapter(DressCodeAdapter adapter);

    void showError(String err);

    void setButtonFav(boolean isFav);
    void favAdded();
    void favRemoved();
}

package mx.app.fashionme.interactor.interfaces;


import java.util.ArrayList;

import mx.app.fashionme.adapter.DressCodeSliderAdapter;
import mx.app.fashionme.pojo.ImageTrend;

public interface IVisageDetailInteractor {

    void getAdapter(ArrayList<ImageTrend> images, VisageDetailListener listener);

    interface VisageDetailListener {

        void onGetAdapter(DressCodeSliderAdapter adapter);
    }
}

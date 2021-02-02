package mx.app.fashionme.interactor;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.adapter.DressCodeSliderAdapter;
import mx.app.fashionme.interactor.interfaces.IVisageDetailInteractor;
import mx.app.fashionme.pojo.ImageTrend;

public class VisageDetailInteractor implements IVisageDetailInteractor {

    @Override
    public void getAdapter(ArrayList<ImageTrend> images, VisageDetailListener listener) {
        listener.onGetAdapter(new DressCodeSliderAdapter(images));
    }
}

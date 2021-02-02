package mx.app.fashionme.view.interfaces;


import java.util.ArrayList;

import mx.app.fashionme.adapter.DressCodeSliderAdapter;
import mx.app.fashionme.pojo.ImageTrend;

public interface IVisageDetailView {

    void setAdapterToSlider(DressCodeSliderAdapter adapter);

    void setTitle(String name);

    void setDescription(String desc);

    void openFullscreenImage(ArrayList<ImageTrend> images, int position);
}

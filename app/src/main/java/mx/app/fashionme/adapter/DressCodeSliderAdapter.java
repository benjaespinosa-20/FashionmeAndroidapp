package mx.app.fashionme.adapter;

import java.util.ArrayList;

import mx.app.fashionme.pojo.ImageTrend;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class DressCodeSliderAdapter extends SliderAdapter {

    private ArrayList<ImageTrend> images;

    public DressCodeSliderAdapter(ArrayList<ImageTrend> images) {
        this.images = images;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        for (int i = 0; i<=images.size(); i++) {
            if (position == i) {
                imageSlideViewHolder.bindImageSlide(images.get(i).getUrlImage());
            }
        }
        /*
        switch (position) {
            case 0:
                imageSlideViewHolder.bindImageSlide(images.get(position).getUrlImage());
                break;
            case 1:
                imageSlideViewHolder.bindImageSlide("https://assets.materialup.com/uploads/20ded50d-cc85-4e72-9ce3-452671cf7a6d/preview.jpg");
                break;
            case 2:
                imageSlideViewHolder.bindImageSlide("https://assets.materialup.com/uploads/76d63bbc-54a1-450a-a462-d90056be881b/preview.png");
                break;
        }
        */
    }
}

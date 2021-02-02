package mx.app.fashionme.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.ImageTrend;
import mx.app.fashionme.pojo.WayDressing;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.utils.PicassoImageLoadingService;
import mx.app.fashionme.view.FullscreenActivity;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

public class DressCodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = DressCodeAdapter.class.getSimpleName();
    private WayDressing wayDressing;
    private Activity activity;

    private static final int STATIC_CARD = 0;
    private static final int DYNAMIC_CARD = 1;

    public DressCodeAdapter(WayDressing wayDressing, Activity activity) {
        this.wayDressing = wayDressing;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == STATIC_CARD) {
            View v1 = inflater.inflate(R.layout.cardview_image_content_dress_code, parent, false);
            viewHolder = new ViewHolderDC1(v1);
        } else {
            View v2 = inflater.inflate(R.layout.cardview_dress_code_image_slider, parent, false);
            viewHolder = new ViewHolderDC2(v2, activity.getApplicationContext());
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == STATIC_CARD) {
            ViewHolderDC1 vh1 = (ViewHolderDC1) holder;
            configureViewHolder1(vh1);

        } else if (getItemViewType(position) == DYNAMIC_CARD) {
            ViewHolderDC2 vh2 = (ViewHolderDC2) holder;
            configureViewHolder2(vh2, position);
        }
    }

    @Override
    public int getItemCount() {
        return wayDressing.getDreesCodes().getData().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return STATIC_CARD;
        } else {
            return DYNAMIC_CARD;
        }
    }

    private void configureViewHolder1(ViewHolderDC1 vh1) {
        Picasso.get().load(wayDressing.getUrlImage()).into(vh1.ivDC);

        vh1.ivDC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
                View mView = activity.getLayoutInflater().inflate(R.layout.dialog_image_zoom, null);

                final PhotoView photoView = mView.findViewById(R.id.imageZoom);

                Picasso.get()
                        .load(wayDressing.getUrlImage())
                        .into(photoView);

                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

                DisplayMetrics metrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int width = metrics.widthPixels; // ancho absoluto en pixels
                int height = metrics.heightPixels; // alto absoluto en pixels

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

                lp.copyFrom(mDialog.getWindow().getAttributes());
                lp.width = width;
                lp.height = height/3;
                mDialog.getWindow().setAttributes(lp);
            }
        });

        String lang = activity.getResources().getString(R.string.app_language);

        switch (lang) {
            case "spanish":
                vh1.getTvDCTitle().setText(wayDressing.getSpanish().getTitle());
                vh1.getTvDCDesc().setText(wayDressing.getSpanish().getDesc());
                break;

            case "english":
                vh1.getTvDCTitle().setText(wayDressing.getEnglish().getTitle());
                vh1.getTvDCDesc().setText(wayDressing.getEnglish().getDesc());
                break;

            default:
                vh1.getTvDCTitle().setText(wayDressing.getSpanish().getTitle());
                vh1.getTvDCDesc().setText(wayDressing.getSpanish().getDesc());
                break;
        }
    }

    private void configureViewHolder2(ViewHolderDC2 vh2, int position) {
        if (wayDressing.getDreesCodes().getData() != null) {
            if (wayDressing.getDreesCodes().getData().get(position - 1).getGenre().equals(SessionPrefs.get(activity).getGenre())){
                if (wayDressing.getDreesCodes().getData().get(position - 1).getImages() != null){
                    ArrayList<ImageTrend> images = wayDressing.getDreesCodes().getData().get(position - 1).getImages().getData();
                    vh2.getSliderDC().setAdapter(new DressCodeSliderAdapter(images));
                    vh2.getSliderDC().setOnSlideClickListener(new OnSlideClickListener() {
                        @Override
                        public void onSlideClick(int position) {
                            Intent intent = new Intent(activity, FullscreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putParcelableArrayListExtra(Constants.IMAGES_VISAGE_MAKEUP_HAIR, images);
                            intent.putExtra(Constants.POSITION_IMAGE, position);
                            activity.startActivity(intent);
                        }
                    });
                }
                vh2.getTvDCName().setText(wayDressing.getDreesCodes().getData().get(position - 1).getSpanish().getName());
            } else {
                vh2.getCvDressCodeSlider().setVisibility(View.INVISIBLE);
            }
        }
    }

    public static class ViewHolderDC1 extends RecyclerView.ViewHolder {

        private ImageView ivDC;
        private TextView tvDCTitle;
        private TextView tvDCDesc;

        public ViewHolderDC1(View itemView) {
            super(itemView);
            ivDC        = itemView.findViewById(R.id.ivDC);
            tvDCTitle   = itemView.findViewById(R.id.tvDCTitle);
            tvDCDesc    = itemView.findViewById(R.id.tvDCDesc);
        }

        public ImageView getIvDC() {
            return ivDC;
        }

        public void setIvDC(ImageView ivDC) {
            this.ivDC = ivDC;
        }

        public TextView getTvDCTitle() {
            return tvDCTitle;
        }

        public void setTvDCTitle(TextView tvDCTitle) {
            this.tvDCTitle = tvDCTitle;
        }

        public TextView getTvDCDesc() {
            return tvDCDesc;
        }

        public void setTvDCDesc(TextView tvDCDesc) {
            this.tvDCDesc = tvDCDesc;
        }
    }

    public static class ViewHolderDC2 extends RecyclerView.ViewHolder {

        private Slider sliderDC;
        private TextView tvDCName;
        private CardView cvDressCodeSlider;

        public ViewHolderDC2(View itemView, Context context) {
            super(itemView);
            Slider.init(new PicassoImageLoadingService(context));
            sliderDC            = itemView.findViewById(R.id.sliderDCImage);
            tvDCName            = itemView.findViewById(R.id.tvDCSliderName);
            cvDressCodeSlider   = itemView.findViewById(R.id.cvDressCodeSlider);
        }

        public Slider getSliderDC() {
            return sliderDC;
        }

        public void setSliderDC(Slider sliderDC) {
            this.sliderDC = sliderDC;
        }

        public TextView getTvDCName() {
            return tvDCName;
        }

        public void setTvDCName(TextView tvDCName) {
            this.tvDCName = tvDCName;
        }

        public CardView getCvDressCodeSlider() {
            return cvDressCodeSlider;
        }

        public void setCvDressCodeSlider(CardView cvDressCodeSlider) {
            this.cvDressCodeSlider = cvDressCodeSlider;
        }
    }
}

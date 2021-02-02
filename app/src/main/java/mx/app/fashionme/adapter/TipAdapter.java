package mx.app.fashionme.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.ImageTrend;
import mx.app.fashionme.pojo.Tip;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.view.FullscreenActivity;

public class TipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = TipAdapter.class.getSimpleName();
    private Tip tip;
    private Activity activity;

    private static final int STATIC_CARD = 0;
    private static final int DYNAMIC_CARD = 1;

    public TipAdapter(Tip tip, Activity activity) {
        this.tip = tip;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == STATIC_CARD) {
            View v2 = inflater.inflate(R.layout.cardview_content_trend_tip, parent, false);
            viewHolder = new ViewHolderTip2(v2);
        } else {
            View v1 = inflater.inflate(R.layout.cardview_image_trend_tip, parent, false);
            viewHolder = new ViewHolderTip1(v1);
        }

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == DYNAMIC_CARD) {
            ViewHolderTip1 vh1 = (ViewHolderTip1) holder;
            configureViewHolder1(vh1, position);
        } else if (getItemViewType(position) == STATIC_CARD) {
            ViewHolderTip2 vh2 = (ViewHolderTip2) holder;
            configureViewHolder2(vh2);
        }
    }

    @Override
    public int getItemCount() {
        return tip.getImages().getData().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return STATIC_CARD;
        } else {
            return DYNAMIC_CARD;
        }
    }

    private void configureViewHolder1(ViewHolderTip1 vh1, int position) {
        ImageTrend image = tip.getImages().getData().get(position-1);
        Picasso.get().load(image.getUrlImage()).into(vh1.ivTip);

        vh1.ivTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFullscreen = new Intent(activity, FullscreenActivity.class);
                intentFullscreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentFullscreen.putParcelableArrayListExtra(Constants.IMAGES_VISAGE_MAKEUP_HAIR, tip.getImages().getData());
                intentFullscreen.putExtra(Constants.POSITION_IMAGE, (position -1));
                activity.startActivity(intentFullscreen);
            }
        });
    }

    private void configureViewHolder2(ViewHolderTip2 vh2) {
        String languaje = activity.getResources().getString(R.string.app_language);

        switch (languaje) {
            case "spanish":
                vh2.getTvTitleTip().setText(tip.getSpanish().getTitle());
                vh2.getTvDescriptionTip().setText(tip.getSpanish().getDesc());
                break;

            case "english":
                vh2.getTvTitleTip().setText(tip.getEnglish().getTitle());
                vh2.getTvDescriptionTip().setText(tip.getEnglish().getDesc());
                break;

            default:
                vh2.getTvTitleTip().setText(tip.getSpanish().getTitle());
                vh2.getTvDescriptionTip().setText(tip.getSpanish().getDesc());
                break;
        }
    }

    public static class ViewHolderTip1 extends RecyclerView.ViewHolder {

        private ImageView ivTip;

        public ViewHolderTip1(View itemView) {
            super(itemView);
            ivTip = itemView.findViewById(R.id.ivImageTrendTip);
        }

        public ImageView getIvTip() {
            return ivTip;
        }

        public void setIvTip(ImageView ivTip) {
            this.ivTip = ivTip;
        }
    }

    public static class ViewHolderTip2 extends RecyclerView.ViewHolder {

        private TextView tvTitleTip, tvDescriptionTip;

        public ViewHolderTip2(View itemView) {
            super(itemView);
            tvTitleTip          = itemView.findViewById(R.id.tvTitleTrendTip);
            tvDescriptionTip    = itemView.findViewById(R.id.tvDescTrendTip);
        }

        public TextView getTvTitleTip() {
            return tvTitleTip;
        }

        public void setTvTitleTip(TextView tvTitleTip) {
            this.tvTitleTip = tvTitleTip;
        }

        public TextView getTvDescriptionTip() {
            return tvDescriptionTip;
        }

        public void setTvDescriptionTip(TextView tvDescriptionTip) {
            this.tvDescriptionTip = tvDescriptionTip;
        }


    }
}

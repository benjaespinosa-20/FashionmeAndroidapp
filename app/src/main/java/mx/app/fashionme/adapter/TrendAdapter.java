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

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.squareup.picasso.Picasso;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.ImageTrend;
import mx.app.fashionme.pojo.Trend;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.view.FullscreenActivity;

public class TrendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = TrendAdapter.class.getSimpleName();
    private Trend trend;
    private Activity activity;
    private PhotoViewAttacher photoViewAttacher;


    private static final int STATIC_CARD = 0;
    private static final int DYNAMIC_CARD = 1;

    public TrendAdapter(Trend trend, Activity activity) {
        this.trend = trend;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == STATIC_CARD){
            View v2 = inflater.inflate(R.layout.cardview_content_trend_tip, parent, false);
            viewHolder = new ViewHolder2(v2);

        } else {
            View v1 = inflater.inflate(R.layout.cardview_image_trend_tip, parent, false);
            viewHolder = new ViewHolder1(v1);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == DYNAMIC_CARD) {
            ViewHolder1 vh1 = (ViewHolder1) holder;
            configureViewHolder1(vh1, position);
        } else if (getItemViewType(position) == STATIC_CARD){
            ViewHolder2 vh2 = (ViewHolder2) holder;
            configureViewHolder2(vh2);
        }
    }

    @Override
    public int getItemCount() {
        return trend.getImages().getData().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return STATIC_CARD;
        } else {
            return DYNAMIC_CARD;
        }
    }

    private void configureViewHolder1(ViewHolder1 vh1, int position) {
        ImageTrend imageTrend = (ImageTrend) trend.getImages().getData().get(position-1);
        Picasso.get().load(imageTrend.getUrlImage()).into(vh1.ivTrend);
        vh1.ivTrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFullscreen = new Intent(activity, FullscreenActivity.class);
                intentFullscreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentFullscreen.putParcelableArrayListExtra(Constants.IMAGES_VISAGE_MAKEUP_HAIR, trend.getImages().getData());
                intentFullscreen.putExtra(Constants.POSITION_IMAGE, (position - 1));
                activity.startActivity(intentFullscreen);
            }
        });
    }

    private void configureViewHolder2(ViewHolder2 vh2) {
        String languaje = activity.getResources().getString(R.string.app_language);

        switch (languaje){
            case "spanish":
                vh2.getTvTitle().setText(trend.getSpanish().getTitle());
                vh2.getTvDescription().setText(trend.getSpanish().getDesc());
                break;
            case "english":
                vh2.getTvTitle().setText(trend.getEnglish().getTitle());
                vh2.getTvDescription().setText(trend.getEnglish().getDesc());
                break;
            default:
                vh2.getTvTitle().setText(trend.getSpanish().getTitle());
                vh2.getTvDescription().setText(trend.getSpanish().getDesc());
                break;
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {

        private ImageView ivTrend;

        public ViewHolder1(View itemView) {
            super(itemView);
            ivTrend = itemView.findViewById(R.id.ivImageTrendTip);
        }

        public ImageView getIvTrend() {
            return ivTrend;
        }

        public void setIvTrend(ImageView ivTrend) {
            this.ivTrend = ivTrend;
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDescription;

        public ViewHolder2(View itemView) {
            super(itemView);
            tvTitle         = itemView.findViewById(R.id.tvTitleTrendTip);
            tvDescription   = itemView.findViewById(R.id.tvDescTrendTip);
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public void setTvTitle(TextView tvTitle) {
            this.tvTitle = tvTitle;
        }

        public TextView getTvDescription() {
            return tvDescription;
        }

        public void setTvDescription(TextView tvDescription) {
            this.tvDescription = tvDescription;
        }
    }
}

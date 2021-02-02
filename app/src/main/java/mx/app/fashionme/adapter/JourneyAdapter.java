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

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.pojo.ImageTrend;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.view.FullscreenActivity;

public class JourneyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = JourneyAdapter.class.getSimpleName();
    private Journey journey;
    private Activity activity;

    private static final int STATIC_CARD = 0;
    private static final int DYNAMIC_CARD = 1;

    public JourneyAdapter(Journey journey, Activity activity) {
        this.journey    = journey;
        this.activity   = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == STATIC_CARD) {
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
        } else if (getItemViewType(position) == STATIC_CARD) {
            ViewHolder2 vh2 = (ViewHolder2) holder;
            configureViewHolder2(vh2);
        }
    }

    @Override
    public int getItemCount() {
        return journey.getImages().getData().size() + 1;
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
        ImageTrend image = journey.getImages().getData().get(position-1);
        Picasso.get().load(image.getUrlImage()).into(vh1.ivTIp);

        vh1.ivTIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFullscreen = new Intent(activity, FullscreenActivity.class);
                intentFullscreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentFullscreen.putParcelableArrayListExtra(Constants.IMAGES_VISAGE_MAKEUP_HAIR, journey.getImages().getData());
                intentFullscreen.putExtra(Constants.POSITION_IMAGE, (position -1));
                activity.startActivity(intentFullscreen);
            }
        });
    }

    private void configureViewHolder2(ViewHolder2 vh2) {
        String lang = activity.getResources().getString(R.string.app_language);

        switch (lang) {
            case "spanish":
                vh2.tvTitle.setText(journey.getSpanish().getTitle());
                vh2.tvDesc.setText(journey.getSpanish().getDesc());
                break;
            case "english":
                vh2.tvTitle.setText(journey.getEnglish().getTitle());
                vh2.tvDesc.setText(journey.getEnglish().getDesc());
                break;
            default:
                vh2.tvTitle.setText(journey.getSpanish().getTitle());
                vh2.tvDesc.setText(journey.getSpanish().getDesc());
                break;
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImageTrendTip)
        ImageView ivTIp;

        public ViewHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitleTrendTip)
        TextView tvTitle;

        @BindView(R.id.tvDescTrendTip)
        TextView tvDesc;

        public ViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

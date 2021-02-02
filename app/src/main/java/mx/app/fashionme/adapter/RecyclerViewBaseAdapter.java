package mx.app.fashionme.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Makeup;
import mx.app.fashionme.pojo.Visage;
import mx.app.fashionme.presenter.MakeupPresenter;
import mx.app.fashionme.presenter.VisagePresenter;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.view.VisageDetailActivity;

public class RecyclerViewBaseAdapter extends RecyclerView.Adapter<RecyclerViewBaseAdapter.ViewHolder> {

    private ArrayList<Object> data;
    private Activity activity;
    private String typeMH;

    public RecyclerViewBaseAdapter(ArrayList<Object> data, Activity activity, String typeMH) {
        this.data    = data;
        this.activity   = activity;
        this.typeMH = typeMH;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_ways_dressing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Object object = data.get(position);

        if (object instanceof Visage ) {
            Visage visage = (Visage) object;

            if (visage.getImages().getData().size() > 0) {
                Picasso.get()
                        .load(visage.getImages().getData().get(0).getUrlImage())
                        .into(holder.ivVisage);
            }

            // Language
            switch (activity.getResources().getString(R.string.app_language)) {
                case "spanish":
                    holder.tvName.setText(visage.getSpanish().getName());
                    break;
                case "english":
                    holder.tvName.setText(visage.getEnglish().getName());
                    break;
                default:
                    holder.tvName.setText(visage.getSpanish().getName());
                    break;
            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, VisageDetailActivity.class);
                    intent.putExtra(VisagePresenter.TYPE_KEY, VisagePresenter.TYPE_VALUE);
                    intent.putExtra(VisagePresenter.OBJ_SPANISH, visage.getSpanish());
                    intent.putExtra(VisagePresenter.OBJ_ENGLISH, visage.getEnglish());
                    intent.putParcelableArrayListExtra(VisagePresenter.IMAGES_VISAGE, visage.getImages().getData());
                    activity.startActivity(intent);
                }
            });
        }

        if (object instanceof Makeup) {
            Makeup makeup= (Makeup) object;

            if (makeup.getImages().getData().size() > 0) {
                Picasso.get()
                        .load(makeup.getImages().getData().get(0).getUrlImage())
                        .into(holder.ivVisage);
            }

            // Language
            switch (activity.getResources().getString(R.string.app_language)) {
                case "spanish":
                    holder.tvName.setText(makeup.getSpanish().getName());
                    break;
                case "english":
                    holder.tvName.setText(makeup.getEnglish().getName());
                    break;
                default:
                    holder.tvName.setText(makeup.getSpanish().getName());
                    break;
            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, VisageDetailActivity.class);
                    intent.putExtra(MakeupPresenter.TYPE_KEY, MakeupPresenter.TYPE_VALUE);
                    intent.putExtra(MakeupPresenter.OBJ_SPANISH, makeup.getSpanish());
                    intent.putExtra(MakeupPresenter.OBJ_ENGLISH, makeup.getEnglish());

                    if (typeMH != null){
                        if (typeMH.equals(Constants.VALUE_MH_M))
                            intent.putExtra(MakeupPresenter.TYPE_KEY_M, MakeupPresenter.TYPE_VALUE_M);
                    }

                    intent.putParcelableArrayListExtra(MakeupPresenter.IMAGES_VISAGE, makeup.getImages().getData());
                    intent.putParcelableArrayListExtra(MakeupPresenter.VIDEOS_VISAGE, makeup.getVideos().getData());
                    intent.putParcelableArrayListExtra(MakeupPresenter.URLS_VISAGE, makeup.getUrls().getData());

                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivVisage;
        private TextView tvName;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ivVisage    = itemView.findViewById(R.id.ivWay);
            tvName      = itemView.findViewById(R.id.tvWayName);
            cardView    = itemView.findViewById(R.id.cvWayDressing);
        }
    }
}

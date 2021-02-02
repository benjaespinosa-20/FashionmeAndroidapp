package mx.app.fashionme.adapter;

import android.app.Activity;
import android.content.Intent;
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
import mx.app.fashionme.pojo.WayDressing;
import mx.app.fashionme.view.DressCode;
import mx.app.fashionme.view.WayDressingActivity;

public class WayDressingAdapter extends RecyclerView.Adapter<WayDressingAdapter.WayDressingViewHolder> {
    ArrayList<WayDressing> ways;
    Activity activity;

    public WayDressingAdapter(ArrayList<WayDressing> ways, Activity activity) {
        this.ways = ways;
        this.activity = activity;
    }

    @Override
    public WayDressingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_ways_dressing, parent, false);
        return new WayDressingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(WayDressingViewHolder holder, int position) {
        final WayDressing wayDressing = ways.get(position);

        Picasso.get()
                .load(wayDressing.getUrlImage())
                .into(holder.ivWay);

        // Language
        String lang = activity.getResources().getString(R.string.app_language);
        switch (lang) {
            case "spanish":
                holder.tvWayName.setText(wayDressing.getSpanish().getTitle());
                break;
            case "english":
                holder.tvWayName.setText(wayDressing.getEnglish().getTitle());
                break;
            default:
                holder.tvWayName.setText(wayDressing.getSpanish().getTitle());
                break;
        }

        holder.cvWayDressing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DressCode.class);
                intent.putExtra(WayDressingActivity.DRESS_CODE, wayDressing.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ways.size();
    }

    public static class WayDressingViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivWay;
        private TextView tvWayName;
        private CardView cvWayDressing;

        public WayDressingViewHolder(View itemView) {
            super(itemView);
            ivWay           = itemView.findViewById(R.id.ivWay);
            tvWayName       = itemView.findViewById(R.id.tvWayName);
            cvWayDressing   = itemView.findViewById(R.id.cvWayDressing);
        }
    }
}

package mx.app.fashionme.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.ImageTrend;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.view.FullscreenActivity;

public class MyClosetAdapter extends RecyclerView.Adapter<MyClosetAdapter.ViewHolder> {

    private List<Clothe> list;
    private Activity activity;

    public MyClosetAdapter(List<Clothe> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_my_closet_gallery,
                        parent,
                        false
                );
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get()
                .load(list.get(position).getUrlImage())
                .placeholder(R.drawable.ic_camera_photo)
                .error(R.drawable.ic_photo_camera)
                .fit()
                .into(holder.MyClosetImagePhoto);
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }

        return list.size();
    }

    private ArrayList<ImageTrend> changeImages(List<Clothe> list) {
        ArrayList<ImageTrend> newData = new ArrayList<>();

        for (Clothe clothe:list){
            newData.add(new ImageTrend(clothe.getId(), clothe.getUrlImage()));
        }

        return newData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.my_closet_image_photo)
        ImageView MyClosetImagePhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, FullscreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putParcelableArrayListExtra(Constants.IMAGES_VISAGE_MAKEUP_HAIR, changeImages(list));
            intent.putExtra(Constants.POSITION_IMAGE, getAdapterPosition());
            activity.startActivity(intent);
        }
    }
}

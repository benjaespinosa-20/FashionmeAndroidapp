package mx.app.fashionme.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.klinker.android.simple_videoview.SimpleVideoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Video;


public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {

    private ArrayList<Video> data;
    private SimpleVideoView currentlyPlaying;

    public MediaAdapter(ArrayList<Video> data) {
        this.data       = data;
    }

    public void releaseVideo() {
        if (currentlyPlaying != null) {
            currentlyPlaying.release();
            currentlyPlaying.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.get().load(R.drawable.ic_play_circle_outline_48dp_black).into(holder.play);

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.video != currentlyPlaying) {
                    releaseVideo();

                    holder.video.setVisibility(View.VISIBLE);
                    holder.video.start(data.get(position).getUrlImage());
                    currentlyPlaying = holder.video;
                }

            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_media_video, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView play;
        public SimpleVideoView video;

        public ViewHolder(View itemView) {
            super(itemView);
            play            = itemView.findViewById(R.id.play_button);
            video           = itemView.findViewById(R.id.video);
        }
    }
}

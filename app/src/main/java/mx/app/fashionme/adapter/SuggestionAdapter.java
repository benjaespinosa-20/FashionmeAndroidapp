package mx.app.fashionme.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.DiffUtil;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IMakeLookView;

/**
 * Created by heriberto on 11/04/18.
 */

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    private static String TAG = "SuggestionAdapter";

    private ArrayList<Clothe> clothes;
    private Context context;
    private Activity activity;
    private IMakeLookView view;

    public SuggestionAdapter(ArrayList<Clothe> clothes, Activity activity, Context context, IMakeLookView view) {
        this.clothes = clothes;
        this.activity = activity;
        this.context = context;
        this.view = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_suggestion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Clothe clothe = clothes.get(position);
        Picasso.get()
                .load(clothe.getUrlImage())
                .into(holder.imgSuggestion);

        holder.cvSuggestion.getLayoutParams().width = Utils.getWidthCV(activity, Utils.COLUMNS_MAKE_LOOK);

        holder.cvSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view != null){
                    view.setStickerImage(clothe.getUrlImage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clothes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgSuggestion;
        private CardView cvSuggestion;

        public ViewHolder(View itemView) {
            super(itemView);

            imgSuggestion   = itemView.findViewById(R.id.imgSuggestion);
            cvSuggestion    = itemView.findViewById(R.id.cvSuggestion);
        }
    }
}

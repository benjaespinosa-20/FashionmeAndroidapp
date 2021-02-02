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
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.ClotheDetailActivity;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ViewHolder> {

    private ArrayList<Clothe> clothes;
    private Activity activity;

    public RecommendationAdapter(ArrayList<Clothe> clothes, Activity activity) {
        this.clothes = clothes;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_clothe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Clothe clothe = clothes.get(position);

        Picasso
                .get()
                .load(clothe.getUrlImage())
                .into(holder.imageView);

        String language = activity.getResources().getString(R.string.app_language);
        switch (language){
            case "spanish":
                holder.tvName.setText(clothe.getSpanish().getName());
                break;
            case "english":
                holder.tvName.setText(clothe.getEnglish().getName());
                break;
            default:
                holder.tvName.setText(clothe.getSpanish().getName());
                break;
        }

        holder.cvClothe.getLayoutParams().width = Utils.getWidthCV(activity, Utils.COLUMNS_RECOMMENDATIONS);

        if (!clothe.getClothePrice().equals("0")) {
            holder.cvClothe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ClotheDetailActivity.class);
                    intent.putExtra("urlImage", clothe.getUrlImage());

                    intent.putExtra("spanishName", clothe.getSpanish().getName());
                    intent.putExtra("englishName", clothe.getEnglish().getName());

                    intent.putExtra("price", clothe.getClothePrice());
                    intent.putExtra("id", clothe.getId());
                    intent.putExtra("link", clothe.getClotheLink());

                    if (clothe.getBrands().getData().size() > 0){
                        intent.putExtra("brand", clothe.getBrands().getData().get(0).getBrand());
                    } else {
                        intent.putExtra("brand", "S/M");
                    }
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return clothes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView tvName;
        private CardView cvClothe;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView   = itemView.findViewById(R.id.imageClotheCV);
            tvName      = itemView.findViewById(R.id.tvNameClotheCV);
            cvClothe    = itemView.findViewById(R.id.cvClothe);
        }
    }
}

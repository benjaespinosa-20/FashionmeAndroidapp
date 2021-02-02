package mx.app.fashionme.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.app.fashionme.view.ClotheDetailActivity;
import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Clothe;

public class ClotheAdapter extends RecyclerView.Adapter<ClotheAdapter.ClotheViewHolder>{

    private ArrayList<Clothe> arrayListClothe;
    private Activity activity;

    public ClotheAdapter(ArrayList<Clothe> arrayListClothe, Activity activity) {
        this.arrayListClothe = arrayListClothe;
        this.activity = activity;
    }

    @Override
    public ClotheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_clothe, parent, false);
        return new ClotheViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClotheViewHolder holder, int position) {
        final Clothe clothe = arrayListClothe.get(position);

        Picasso.get()
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

        if (clothe.isInCar()) {
            holder.cvClothe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clothe.getClotheLink() == null || clothe.getClotheLink().equals("not exist")) {
                        Toast.makeText(activity, "No disponible.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clothe.getClotheLink()));
                        activity.startActivity(browserIntent);
                    }
                }
            });
        } else {
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
        return arrayListClothe.size();
    }

    public static class ClotheViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView tvName;
        private CardView cvClothe;

        public ClotheViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageClotheCV);
            tvName = itemView.findViewById(R.id.tvNameClotheCV);
            cvClothe = itemView.findViewById(R.id.cvClothe);
        }
    }
}

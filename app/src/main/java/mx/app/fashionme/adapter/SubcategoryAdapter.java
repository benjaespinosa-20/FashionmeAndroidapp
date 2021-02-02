package mx.app.fashionme.adapter;

import android.app.Activity;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mx.app.fashionme.view.ClotheActivity;
import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Subcategory;

/**
 * Created by heriberto on 12/03/18.
 */

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewHolder> {
    ArrayList<Subcategory> subcategories;
    Activity activity;

    public SubcategoryAdapter(ArrayList<Subcategory> subcategories, Activity activity) {
        this.subcategories = subcategories;
        this.activity = activity;
    }

    @Override
    public SubcategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_subcategory, parent, false);
        return new SubcategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubcategoryViewHolder subcategoryViewHolder, int position) {
        final Subcategory subcategory = subcategories.get(position);

        String languaje = activity.getResources().getString(R.string.app_language);
        switch (languaje) {
            case "spanish":
                subcategoryViewHolder.tvNameSubcategory.setText(subcategory.getSpanish().getName());
                break;
            case "english":
                subcategoryViewHolder.tvNameSubcategory.setText(subcategory.getEnglish().getName());
                break;
            default:
                subcategoryViewHolder.tvNameSubcategory.setText(subcategory.getSpanish().getName());
                break;
        }

        subcategoryViewHolder.cardViewSubcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ClotheActivity.class);
                intent.putExtra("idSubcategory", subcategory.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    public static class SubcategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNameSubcategory;
        private CardView cardViewSubcategory;

        public SubcategoryViewHolder(View itemView) {
            super(itemView);
            tvNameSubcategory = (TextView) itemView.findViewById(R.id.tvNameSubcategoryCV);
            cardViewSubcategory = itemView.findViewById(R.id.cvSubcategory);
        }
    }
}

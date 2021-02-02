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

import mx.app.fashionme.pojo.Category;
import mx.app.fashionme.R;
import mx.app.fashionme.view.SubcategoryActivity;

/**
 * Created by heriberto on 12/03/18.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    ArrayList<Category> categories;
    Activity activity;

    public CategoryAdapter(ArrayList<Category> categories, Activity activity) {
        this.categories = categories;
        this.activity = activity;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_category, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder categoryViewHolder, int position) {
        final Category category = categories.get(position);
        //categoryViewHolder.imgImage.setImageResource(category.getUrlImage());
        Picasso.get()
                .load(category.getUrlImage())
                .into(categoryViewHolder.imgImage);

        // If language
        String language = activity.getResources().getString(R.string.app_language);
        switch (language) {
            case "spanish":
                categoryViewHolder.tvName.setText(category.getSpanish().getName());
                break;
            case "english":
                categoryViewHolder.tvName.setText(category.getEnglish().getName());
                break;
            default:
                categoryViewHolder.tvName.setText(category.getSpanish().getName());
                break;
        }

        categoryViewHolder.cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SubcategoryActivity.class);
                intent.putExtra("idCategory", category.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgImage;
        private TextView tvName;
        private CardView cvCategory;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            imgImage =  (ImageView) itemView.findViewById(R.id.imageCategoryCV);
            tvName =    (TextView) itemView.findViewById(R.id.tvNameCategoryCV);
            cvCategory = itemView.findViewById(R.id.cvCategory);
        }
    }
}

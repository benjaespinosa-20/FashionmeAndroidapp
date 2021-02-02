package mx.app.fashionme.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.ItemHome;

public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<ItemHome> items;
    private final boolean premium;
    private final ClickModuleListener mListener;

    private int iconColor;
    private final String ICON_COLOR_KEY = "icon-key";

    public HomeItemAdapter(Context context, ArrayList<ItemHome> items, boolean premium, ClickModuleListener listener) {
        this.context    = context;
        this.items      = items;
        this.premium    = premium;
        this.mListener  = listener;

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //holder.imgItem.setImageResource(items.get(position).getIcon());
        Picasso.get().load(items.get(position).getIcon()).into(holder.imgItem);
        holder.tvItemName.setText(items.get(position).getTitle());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        iconColor = preferences.getInt(ICON_COLOR_KEY, ContextCompat.getColor(context, R.color.colorPrimaryDark));
        holder.imgItem.setColorFilter(iconColor);

        holder.imgItem.setOnClickListener(view -> {
            holder.imgItem.setColorFilter(context.getResources().getColor(R.color.colorAccent));
            mListener.onClickModule(items.get(position).getId(), premium);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgItem;
        private TextView tvItemName;

        ItemViewHolder(View itemView) {
            super(itemView);

            imgItem     = itemView.findViewById(R.id.imgItemHome);
            tvItemName  = itemView.findViewById(R.id.tvItemName);
        }
    }

    /**
     * Interfaz callback para el manejo de acciones de elementos en la lista
     */
    public interface ClickModuleListener {

        /**
         * Metodo para manejar click de elemento correspondiente
         * @param idItem id del elemento
         * @param isPremium Bandera para saber si el usuario es premium
         */
        void onClickModule(int idItem, boolean isPremium);

    }
}

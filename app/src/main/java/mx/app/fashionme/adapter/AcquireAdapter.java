package mx.app.fashionme.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.viewHolder.RowViewHolder;
import mx.app.fashionme.data.models.acquire.SkuDetailsModel;

public class AcquireAdapter extends RecyclerView.Adapter<RowViewHolder> {

    private List<SkuDetailsModel> mList;
    /**
     * Listener para respuesta onclick
     */
    private RowViewHolder.OnButtonClickListener listener;

    public AcquireAdapter(List<SkuDetailsModel> data, RowViewHolder.OnButtonClickListener listener) {
        this.mList      = data;
        this.listener   = listener;
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.sku_details_row, parent, false);
        return new RowViewHolder(item, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
        SkuDetailsModel price = mList.get(position);
        holder.title.setText(price.getTitle());
        holder.description.setText(price.getDescription());
        holder.price.setText(price.getPrice());
        holder.button.setOnClickListener(
                v -> listener.onButtonClicked(price)
        );
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

}

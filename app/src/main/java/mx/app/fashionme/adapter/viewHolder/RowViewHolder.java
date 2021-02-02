package mx.app.fashionme.adapter.viewHolder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mx.app.fashionme.R;
import mx.app.fashionme.data.models.acquire.SkuDetailsModel;

public final class RowViewHolder extends RecyclerView.ViewHolder {
    public TextView title, description, price;
    public Button button;

    public interface OnButtonClickListener {
        void onButtonClicked(SkuDetailsModel sku);
    }

    public RowViewHolder(final View itemView, final OnButtonClickListener clickListener) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        price = (TextView) itemView.findViewById(R.id.price);
        description = (TextView) itemView.findViewById(R.id.description);
        button = (Button) itemView.findViewById(R.id.state_button);
    }
}

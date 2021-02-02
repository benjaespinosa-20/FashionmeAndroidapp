package mx.app.fashionme.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.pojo.DataRegisterClotheViewModel;

public class DataRegisterClotheAdapter extends RecyclerView.Adapter<DataRegisterClotheAdapter.ViewHolder> {

    private List<DataRegisterClotheViewModel> list;
    private List<DataRegisterClotheViewModel> previousData;

    public DataRegisterClotheAdapter(List<DataRegisterClotheViewModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.data_register_list_item,
                        parent,
                        false
                );
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(list.get(position));

        // in some cases, it will prevent unwanted situations
        holder.checkBoxData.setOnCheckedChangeListener(null);

        // if true, your checkbox will be selected, else unselected
        holder.checkBoxData.setChecked(list.get(position).isSelected());

        holder.checkBoxData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(holder.getAdapterPosition()).setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }

        return list.size();
    }

    public void previousData(List<DataRegisterClotheViewModel> previousData) {
        this.previousData = previousData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.check_box_data)
        CheckBox checkBoxData;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindData(DataRegisterClotheViewModel model) {

            if (previousData != null) {
                for (DataRegisterClotheViewModel previous:previousData) {
                    if (previous.getId() == model.getId()){
                        model.setSelected(true);
                    }
                }
            }

            checkBoxData.setText(model.getName());
        }
    }
}

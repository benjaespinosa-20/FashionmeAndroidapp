package mx.app.fashionme.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Characteristic;

/**
 * Created by desarrollo1 on 14/03/18.
 */

public class CharacteristicAdapter extends ArrayAdapter<Characteristic>{
    private List<Characteristic> characteristics;
    private Activity activity;

    public CharacteristicAdapter(List<Characteristic> characteristics, Activity activity) {
        super(activity, R.layout.listview_characteristic, characteristics);
        this.characteristics = characteristics;
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = activity.getLayoutInflater();
            view = inflator.inflate(R.layout.listview_characteristic, null);

            final CharacteristicViewHolder viewHolder = new CharacteristicViewHolder();

            viewHolder.tvDesc = view.findViewById(R.id.tvDesc);
            viewHolder.tvId = view.findViewById(R.id.idChars);
            viewHolder.chkSelection = view.findViewById(R.id.chkSelection);
            viewHolder.chkSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Characteristic element = (Characteristic) viewHolder.chkSelection.getTag();
                    element.setSelection(buttonView.isChecked());
                }
            });

            view.setTag(viewHolder);
            viewHolder.chkSelection.setTag(characteristics.get(position));
        } else {
            view = convertView;
            ((CharacteristicViewHolder) view.getTag()).chkSelection.setTag(characteristics.get(position));
        }

        CharacteristicViewHolder holder = (CharacteristicViewHolder) view.getTag();
        // Get language
        String lang = getContext().getResources().getString(R.string.app_language);
        switch (lang){
            case "spanish":
                if (characteristics.get(position).getSpanish().getDesc() != null){
                    holder.tvDesc.setText(characteristics.get(position).getSpanish().getDesc());
                } else {
                    holder.tvDesc.setText(characteristics.get(position).getSpanish().getName());
                }
                break;
            case "english":
                if (characteristics.get(position).getEnglish().getDesc() != null) {
                    holder.tvDesc.setText(characteristics.get(position).getEnglish().getDesc());
                } else {
                    holder.tvDesc.setText(characteristics.get(position).getEnglish().getName());
                }
                break;
            default:
                if (characteristics.get(position).getSpanish().getDesc() != null){
                    holder.tvDesc.setText(characteristics.get(position).getSpanish().getDesc());
                } else {
                    holder.tvDesc.setText(characteristics.get(position).getSpanish().getName());
                }
        }

        holder.tvId.setText(characteristics.get(position).getId()+"");
        holder.chkSelection.setChecked(characteristics.get(position).isSelection());

        return view;
    }

    public static class CharacteristicViewHolder {
        private TextView tvDesc, tvId;
        private CheckBox chkSelection;
    }
}

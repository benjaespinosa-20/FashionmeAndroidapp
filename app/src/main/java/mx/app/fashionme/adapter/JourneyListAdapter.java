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

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.view.ShowJourneyActivity;

public class JourneyListAdapter extends RecyclerView.Adapter<JourneyListAdapter.JourneyViewHolder> {

    public static final String ID_JOURNEY = "id_journey";
    private ArrayList<Journey> journeys;
    private Activity activity;

    public JourneyListAdapter(ArrayList<Journey> journeys, Activity activity) {
        this.journeys   = journeys;
        this.activity   = activity;
    }

    @Override
    public JourneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_subcategory, parent, false);
        return new JourneyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JourneyViewHolder holder, int position) {
        final Journey journey = journeys.get(position);

        String lang = activity.getResources().getString(R.string.app_language);

        switch (lang) {
            case "spanish":
                holder.tvNameJourney.setText(journey.getSpanish().getTitle());
                break;
            case "english":
                holder.tvNameJourney.setText(journey.getEnglish().getTitle());
                break;
            default:
                holder.tvNameJourney.setText(journey.getSpanish().getTitle());
                break;
        }

        holder.cvJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ShowJourneyActivity.class);
                intent.putExtra(ID_JOURNEY, journey.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return journeys.size();
    }

    public static class JourneyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNameJourney;
        private CardView cvJourney;

        public JourneyViewHolder(View itemView) {
            super(itemView);
            tvNameJourney   = itemView.findViewById(R.id.tvNameSubcategoryCV);
            cvJourney       = itemView.findViewById(R.id.cvSubcategory);
        }
    }
}

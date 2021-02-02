package mx.app.fashionme.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.Favorite;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.pojo.Tip;
import mx.app.fashionme.pojo.Trend;
import mx.app.fashionme.pojo.WayDressing;
import mx.app.fashionme.view.ClotheDetailActivity;
import mx.app.fashionme.view.DressCode;
import mx.app.fashionme.view.ShowJourneyActivity;
import mx.app.fashionme.view.TipActivity;
import mx.app.fashionme.view.TrendActivity;
import mx.app.fashionme.view.WayDressingActivity;

public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = FavoriteAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Favorite> favorites;

    private final int TYPE_HEADER   = 1;
    private final int TYPE_NORMAL   = 2;

    public FavoriteAdapter(ArrayList<Favorite> favorites, Context context) {
        this.favorites = favorites;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_favorites, parent, false);
            return new RecyclerViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_favorites_header, parent, false);
            return new HeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        String lang = context.getResources().getString(R.string.app_language);

        if (holder instanceof RecyclerViewHolder){
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler_item_show);
            recyclerViewHolder.mView.startAnimation(animation);

            AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
            aa1.setDuration(400);


            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(400);

            int items = getItemCount();
            String name = null;
            String urlImage = null;
            String typeCard = null;
            Object obj = null;

            for (int i = 0; i < favorites.size(); i++) {

                if (favorites.get(i).getTrends() != null){
                    for (int j = 0; j < favorites.get(i).getTrends().getData().size(); j++) {
                        items--;
                        if (items == position) {
                            switch (lang) {
                                case "spanish":
                                    name = favorites.get(i).getTrends().getData().get(j).getSpanish().getTitle();
                                    break;
                                case "english":
                                    name = favorites.get(i).getTrends().getData().get(j).getEnglish().getTitle();
                                    break;
                                default:
                                    name = favorites.get(i).getTrends().getData().get(j).getSpanish().getTitle();
                                    break;
                            }

                            urlImage    = favorites.get(i).getTrends().getData().get(j).getImages().getData().get(0).getUrlImage();
                            typeCard    = "trends";
                            obj         = favorites.get(i).getTrends().getData().get(j);
                        }
                    }
                }

                if (favorites.get(i).getTips() != null) {
                    for (int j = 0; j < favorites.get(i).getTips().getData().size(); j++) {
                        items--;
                        if (items == position) {

                            switch (lang) {
                                case "spanish":
                                    name = favorites.get(i).getTips().getData().get(j).getSpanish().getTitle();
                                    break;
                                case "english":
                                    name = favorites.get(i).getTips().getData().get(j).getEnglish().getTitle();
                                    break;
                                default:
                                    name = favorites.get(i).getTips().getData().get(j).getSpanish().getTitle();
                                    break;
                            }

                            urlImage    = favorites.get(i).getTips().getData().get(j).getImages().getData().get(0).getUrlImage();
                            typeCard    = "tips";
                            obj         = favorites.get(i).getTips().getData().get(j);
                        }
                    }
                }

                if (favorites.get(i).getJourneys() != null) {
                    for (int j = 0; j < favorites.get(i).getJourneys().getData().size(); j++) {
                        items--;
                        if (items == position) {

                            switch (lang) {
                                case "spanish":
                                    name = favorites.get(i).getJourneys().getData().get(j).getSpanish().getTitle();
                                    break;
                                case "english":
                                    name = favorites.get(i).getJourneys().getData().get(j).getEnglish().getTitle();
                                    break;
                                default:
                                    name = favorites.get(i).getJourneys().getData().get(j).getSpanish().getTitle();
                                    break;
                            }

                            urlImage    = favorites.get(i).getJourneys().getData().get(j).getImages().getData().get(0).getUrlImage();
                            typeCard    = "journeys";
                            obj         = favorites.get(i).getJourneys().getData().get(j);
                        }
                    }
                }

                if (favorites.get(i).getClothes() != null) {
                    for (int j = 0; j < favorites.get(i).getClothes().getData().size(); j++) {
                        items--;
                        if (items == position) {

                            switch (lang) {
                                case "spanish":
                                    name = favorites.get(i).getClothes().getData().get(j).getSpanish().getName();
                                    break;
                                case "english":
                                    name = favorites.get(i).getClothes().getData().get(j).getEnglish().getName();
                                    break;
                                default:
                                    name = favorites.get(i).getClothes().getData().get(j).getSpanish().getName();
                                    break;
                            }

                            urlImage    = favorites.get(i).getClothes().getData().get(j).getUrlImage();
                            typeCard    = "clothes";
                            obj         = favorites.get(i).getClothes().getData().get(j);
                        }
                    }
                }

                if (favorites.get(i).getWays_dressing() != null) {
                    for (int j = 0; j < favorites.get(i).getWays_dressing().getData().size(); j++) {
                        items--;
                        if (items == position) {

                            switch (lang) {
                                case "spanish":
                                    name = favorites.get(i).getWays_dressing().getData().get(j).getSpanish().getTitle();
                                    break;
                                case "english":
                                    name = favorites.get(i).getWays_dressing().getData().get(j).getEnglish().getTitle();
                                    break;
                                default:
                                    name = favorites.get(i).getWays_dressing().getData().get(j).getSpanish().getTitle();
                                    break;
                            }

                            urlImage    = favorites.get(i).getWays_dressing().getData().get(j).getUrlImage();
                            typeCard    = "dressing";
                            obj         = favorites.get(i).getWays_dressing().getData().get(j);
                        }
                    }
                }

                items--;
            }

            recyclerViewHolder.tv_recycler_item_1.setText(name);
            if (urlImage != null)
                Picasso.get().load(urlImage).into(recyclerViewHolder.imgItemFav);

            if (typeCard != null) {
                switch (typeCard) {
                    case "dressing":
                        WayDressing wayDressing = (WayDressing) obj;

                        recyclerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, DressCode.class);
                                intent.putExtra(WayDressingActivity.DRESS_CODE, wayDressing.getId());
                                context.startActivity(intent);
                            }
                        });
                        
                        break;


                    case "clothes":
                        Clothe clothe = (Clothe) obj;

                        recyclerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, ClotheDetailActivity.class);
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
                                context.startActivity(intent);
                            }
                        });
                        break;

                    case "journeys":
                        Journey journey = (Journey) obj;

                        recyclerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, ShowJourneyActivity.class);
                                intent.putExtra(JourneyListAdapter.ID_JOURNEY, journey.getId());
                                context.startActivity(intent);
                            }
                        });
                        break;
                        
                    case "tips":
                        Tip tip = (Tip) obj;

                        recyclerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, TipActivity.class);
                                intent.putExtra(TipActivity.ID_TIP, tip.getId());
                                context.startActivity(intent);
                            }
                        });
                        break;
                        
                    case "trends":
                        Trend trend = (Trend) obj;
                        recyclerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, TrendActivity.class);
                                intent.putExtra(TrendActivity.ID_TREND, trend.getId());
                                context.startActivity(intent);
                            }
                        });
                        break;
                        
                    default:
                        break;
                }

            }
        }

        if (holder instanceof HeaderViewHolder) {
            final  HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            int items = getItemCount();
            String type = null;

            for (int i = 0; i < favorites.size(); i++) {

                if (favorites.get(i).getTrends() != null){
                    items-= favorites.get(i).getTrends().getData().size();
                    items--;
                    type = favorites.get(i).getType();
                }


                if (favorites.get(i).getTips() != null) {
                    items-= favorites.get(i).getTips().getData().size();
                    items--;
                    type = favorites.get(i).getType();
                }

                if (favorites.get(i).getJourneys() != null) {
                    items-= favorites.get(i).getJourneys().getData().size();
                    items--;
                    type = favorites.get(i).getType();
                }


                if (favorites.get(i).getClothes() != null) {
                    items-= favorites.get(i).getClothes().getData().size();
                    items--;
                    type = favorites.get(i).getType();
                }


                if (favorites.get(i).getWays_dressing() != null) {
                    items-= favorites.get(i).getWays_dressing().getData().size();
                    items--;
                    type = favorites.get(i).getType();
                }

                if (items == position) {
                    headerViewHolder.header_text.setText(type);
                }

            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        int items = getItemCount();

        for (int i = 0; i < favorites.size(); i++) {

            if (favorites.get(i).getTrends() != null){
                items-= favorites.get(i).getTrends().getData().size();
                items--;
            }

            if (favorites.get(i).getTips() != null) {
                items-= favorites.get(i).getTips().getData().size();
                items--;
            }

            if (favorites.get(i).getJourneys() != null) {
                items-= favorites.get(i).getJourneys().getData().size();
                items--;
            }

            if (favorites.get(i).getClothes() != null) {
                items-= favorites.get(i).getClothes().getData().size();
                items--;
            }

            if (favorites.get(i).getWays_dressing() != null) {
                items-= favorites.get(i).getWays_dressing().getData().size();
                items--;
            }

            if (items == position) {
                return TYPE_HEADER;
            }

        }

        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        int items = 0;
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).getTrends() != null){
                items+= favorites.get(i).getTrends().getData().size();
                items++;
            }


            if (favorites.get(i).getTips() != null) {
                items+= favorites.get(i).getTips().getData().size();
                items++;
            }

            if (favorites.get(i).getJourneys() != null) {
                items+= favorites.get(i).getJourneys().getData().size();
                items++;
            }


            if (favorites.get(i).getClothes() != null) {
                items+= favorites.get(i).getClothes().getData().size();
                items++;
            }


            if (favorites.get(i).getWays_dressing() != null) {
                items+= favorites.get(i).getWays_dressing().getData().size();
                items++;
            }
        }

        return items;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView tv_recycler_item_1;
        private ImageView imgItemFav;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mView               = itemView;
            tv_recycler_item_1  = itemView.findViewById(R.id.tv_recycler_item_1);
            imgItemFav          = itemView.findViewById(R.id.imgItemFav);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView header_text;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            header_text = itemView.findViewById(R.id.header_text);
        }
    }
}

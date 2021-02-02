package mx.app.fashionme.utils;

import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.ItemHome;
import mx.app.fashionme.prefs.SessionPrefs;

public class HomeItems {

    public static ArrayList<ItemHome> listItemHome(Context context) {

        ArrayList<ItemHome> list = new ArrayList<>();

        list.add(new ItemHome(2, R.drawable.new_ic_home_style_black,context.getString(R.string.your_style)));
        list.add(new ItemHome(0, R.drawable.new_ic_home_ideal_closet_black,context.getString(R.string.your_closet_ideal)));
        list.add(new ItemHome(1, R.drawable.new_ic_home_fme_closet_black,context.getString(R.string.closet_fme)));
        list.add(new ItemHome(5, R.drawable.new_ic_home_make_look_black, context.getString(R.string.item_home_create_look)));
        list.add(new ItemHome(4, R.drawable.new_ic_home_recommendation_black, context.getString(R.string.item_home_recommendations)));

        if (SessionPrefs.get(context).getGenre().equals("woman")) {
            list.add(new ItemHome(6, R.drawable.new_ic_home_visage_black, context.getString(R.string.item_home_visagism)));
            list.add(new ItemHome(8, R.drawable.new_ic_home_makeup_black, context.getString(R.string.item_home_makeup)));
        }

        list.add(new ItemHome(9, R.drawable.new_ic_home_hair_black, context.getString(R.string.item_home_hair)));
        list.add(new ItemHome(7, R.drawable.ic_face_black_24dp, context.getString(R.string.user_profile_face)));

        return list;
    }

    public static ArrayList<ItemHome> listItemRecommend(Context context) {
        ArrayList<ItemHome> list = new ArrayList<>();

        list.add(new ItemHome(100, R.drawable.new_ic_home_trend_black,context.getString(R.string.trends)));
        list.add(new ItemHome(110, R.drawable.new_ic_home_dress_codes_black,context.getString(R.string.dress_code)));
        list.add(new ItemHome(120, R.drawable.new_ic_home_tips_black,context.getString(R.string.tips)));
        list.add(new ItemHome(130, R.drawable.new_ic_home_journey_black,context.getString(R.string.item_home_journey)));

        return list;
    }
}

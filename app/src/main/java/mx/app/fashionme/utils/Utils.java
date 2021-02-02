package mx.app.fashionme.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;

import java.util.StringTokenizer;

import mx.app.fashionme.R;

public class Utils {

    public static final String INTENT_EXTRA_COLOR_TEST = "color_extra";

    public static final String EXTRA_FRIO_FRAGMENT = "extra_frio_fragment";

    public static final String EXTRA_CALIDO_FRAGMENT = "extra_calido_fragment";

    public static final int COLUMNS_RECOMMENDATIONS = 2;

    public static final int COLUMNS_MAKE_LOOK = 3;

    public static final String TOOLBAR_COLOR_KEY = "toolbar-key";

    /**
     * Calcule el número de palabras para el contenido compartido. Nota: Esta función no se aplica al cálculo de un solo carácter, porque un solo carácter se redondea a 1
     * @param s
     * @return
     */
    public static long calculateLength(CharSequence s) {
        double len = 0;
        for (int i = 0; i < s.length(); i++) {
            int tmp = (int) s.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    /**
     * Metodo para poner la primer letra en mayuscula de una cadena de string
     * @param name
     * @return
     */
    public static String toCapitalName(String name) {
        if (name == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        StringTokenizer st = new StringTokenizer(name, " ");
        while (st.hasMoreElements()) {
            String ne = (String)st.nextElement();
            if (ne.length() > 0) {
                builder.append(ne.substring(0, 1).toUpperCase());
                builder.append(ne.substring(1).toLowerCase());
                builder.append(' ');
            }
        }
        return builder.toString();
    }

    /**
     * Metodo para calcular el número de columnas a poner en un GridLayoutManager
     * en base a las dimensiones de la pantalla del dispositivo
     * @param context Contexto de la aplicacion
     * @return Número de columnas
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public static int getWidthCV(Activity activity, int columns) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels / columns;
    }

    public static int getTabColor(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(TOOLBAR_COLOR_KEY, ContextCompat.getColor(context, R.color.colorPrimary));
    }
}

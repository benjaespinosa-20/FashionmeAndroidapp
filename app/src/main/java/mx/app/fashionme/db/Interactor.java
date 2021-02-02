package mx.app.fashionme.db;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Category;

/**
 * Created by heriberto on 13/03/18.
 */

public class Interactor {

    private Context context;

    public Interactor(Context context) {
        this.context = context;
    }

    public ArrayList<Category> getData() {
        // DUMMY
        ArrayList<Category> categories = new ArrayList<>();

//        categories.add(new Category(R.drawable.vestidos, "Vestidos", "M"));
//        categories.add(new Category(R.drawable.zapatos, "Calzado", "H"));
//        categories.add(new Category(R.drawable.abrigos, "Tops", "M"));
//        categories.add(new Category(R.drawable.vestidos, "Vestidos", "M"));
//        categories.add(new Category(R.drawable.zapatos, "Calzado", "H"));
//        categories.add(new Category(R.drawable.abrigos, "Tops", "M"));
//        categories.add(new Category(R.drawable.vestidos, "Vestidos", "M"));
//        categories.add(new Category(R.drawable.zapatos, "Calzado", "H"));
//        categories.add(new Category(R.drawable.abrigos, "Tops", "M"));
//        categories.add(new Category(R.drawable.vestidos, "Vestidos", "M"));
//        categories.add(new Category(R.drawable.zapatos, "Calzado", "H"));
//        categories.add(new Category(R.drawable.abrigos, "Tops", "M"));

        return categories;

        // DATABASE
//        DataBase db = new DataBase(context);
//        insert(db);
//        return db.getAll();

    }

    /*
    public void insertCategory(DataBase db) {
        ContentValues values = new ContentValues();
        values.put(ConstantesBaseDatos.TABLE_MASCOTA_NOMBRE, "Catty");
        values.put(ConstantesBaseDatos.TABLE_MASCOTA_FOTO, R.drawable.gato_1);

        db.insert(values);
    }
    */
}

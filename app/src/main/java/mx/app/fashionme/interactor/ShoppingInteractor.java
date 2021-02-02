package mx.app.fashionme.interactor;

import android.content.Context;

import mx.app.fashionme.db.FavConstructor;
import mx.app.fashionme.interactor.interfaces.IShoppingInteractor;

public class ShoppingInteractor implements IShoppingInteractor {
    @Override
    public void getDataFromDB(Context context, ClotheShoppingListener listener) {
        FavConstructor constructor = new FavConstructor(context);
        listener.onGetCart(constructor.getClothesOnCart());
    }
}

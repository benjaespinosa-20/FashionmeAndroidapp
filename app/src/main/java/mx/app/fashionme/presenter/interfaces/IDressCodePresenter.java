package mx.app.fashionme.presenter.interfaces;

public interface IDressCodePresenter {
    void getDataDressCode(int idDressCode);

    void checkFav(int idDressCode);
    void removeFavWay(int idDressCode);
    void addFavWay(int idDressCode);
}

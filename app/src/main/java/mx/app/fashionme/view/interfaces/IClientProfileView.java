package mx.app.fashionme.view.interfaces;

public interface IClientProfileView {

    void showProgress();

    void hideProgress();

    void showLayoutProfile();

    void hideLayoutProfile();

    void showToast(String error);

    void notEmail();

    void setName(String name);

    void setEmail(String email);

    void setGenre(String genre);

    void setColorName(String colorName);

    void setImagePalette(String palette);

    void setBodyType(String bodyType);

    void setStyle(String style);
}

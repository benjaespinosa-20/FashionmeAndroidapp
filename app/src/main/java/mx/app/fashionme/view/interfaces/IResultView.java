package mx.app.fashionme.view.interfaces;

public interface IResultView {

    void setUpPresenter();
    void getResults();
    void setResults(String name, String body, String color, String image);
    void navigateToHomePage();
    void goToBodyActivity();
    void goToTestColorActivity();

    void showProgressBar();

    void hideProgressBar();
}

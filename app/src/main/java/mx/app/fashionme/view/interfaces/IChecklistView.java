package mx.app.fashionme.view.interfaces;

import java.util.ArrayList;

import mx.app.fashionme.pojo.Characteristic;

public interface IChecklistView {
    void setAdapterChecklist(ArrayList<Characteristic> checklist);
    void setToolbarChecklist();
    void setUpPresenterChecklist();
    void showError(String err);
    void showProgressDialog(boolean show);
}

package mx.app.fashionme.presenter.interfaces;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

public interface IVisagePresenter {

    void getVisages();

    SwipeRefreshLayout.OnRefreshListener getOnRefreshListener();

    View.OnClickListener setOnClickListenerTryAgain();

    void setAnalytics();
}

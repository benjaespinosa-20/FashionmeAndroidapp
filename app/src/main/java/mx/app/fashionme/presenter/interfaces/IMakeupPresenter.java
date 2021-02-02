package mx.app.fashionme.presenter.interfaces;

import android.app.Activity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;

public interface IMakeupPresenter {

    void getMakeups();

    SwipeRefreshLayout.OnRefreshListener getOnRefreshListener();

    View.OnClickListener setOnClickListenerTryAgain();

    void setAnalytics(Activity activity);

    //void startActivity(Visage visage);
}

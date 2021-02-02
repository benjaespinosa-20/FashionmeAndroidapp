package mx.app.fashionme.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.MediaAdapter;
import mx.app.fashionme.interactor.MediaVideoInteractor;
import mx.app.fashionme.pojo.Url;
import mx.app.fashionme.pojo.Video;
import mx.app.fashionme.presenter.MediaVideoPresenter;
import mx.app.fashionme.presenter.interfaces.IMediaVideoPresenter;
import mx.app.fashionme.view.interfaces.IMediaVideoView;

public class MediaVideoActivity extends AppCompatActivity implements IMediaVideoView {

    @BindView(R.id.rvVideos)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.llErrors)
    LinearLayout llErrors;

    @BindView(R.id.tvErrorTitle)
    TextView tvErrorTitle;

    @BindView(R.id.tvErrorDescription)
    TextView tvErrorDescription;

    @BindView(R.id.btnTryAgain)
    Button btnTryAgain;

    private MediaAdapter adapter;

    private IMediaVideoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_video);

        ButterKnife.bind(this);
        presenter = new MediaVideoPresenter(getApplicationContext(), MediaVideoActivity.this, this, new MediaVideoInteractor());
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(getLayoutManager());
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );
    }

    private MediaAdapter getAdapter(ArrayList<Video> dataVideo, ArrayList<Url> dataUrl) {
        //ArrayList<Object> list = new ArrayList<>(dataVideo);
//        if (dataVideo != null)
//            list.add(dataVideo);
//        if (dataUrl != null)
//            list.add(dataUrl);

        return new MediaAdapter(dataVideo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getMedia();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (adapter != null) {
            adapter.releaseVideo();
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLinearLayoutError() {
        llErrors.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLinearLayoutError() {
        llErrors.setVisibility(View.GONE);
    }

    @Override
    public void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void offline() {
        tvErrorTitle.setText(getString(R.string.offline));
        tvErrorDescription.setText(R.string.no_internet_connection_description);
        btnTryAgain.setText(R.string.try_again);
        btnTryAgain.setOnClickListener(presenter.setOnClickListenerTryAgain());
    }

    @Override
    public void emptyList() {
        tvErrorTitle.setText(R.string.no_items);
        tvErrorDescription.setText(R.string.no_items_description);
        btnTryAgain.setText(R.string.try_again);
        btnTryAgain.setOnClickListener(presenter.setOnClickListenerTryAgain());
    }

    @Override
    public void error(String error) {
        tvErrorTitle.setText(R.string.ups);
        tvErrorDescription.setText(error);
        btnTryAgain.setText(R.string.try_again);
        btnTryAgain.setOnClickListener(presenter.setOnClickListenerTryAgain());
    }

    @Override
    public void setItems(ArrayList<Video> dataVideo, ArrayList<Url> dataUrl) {

        if (adapter != null) {
            adapter.releaseVideo();
        }

        adapter = getAdapter(dataVideo, dataUrl);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}

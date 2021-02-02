package mx.app.fashionme.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.JourneyAdapter;
import mx.app.fashionme.adapter.JourneyListAdapter;
import mx.app.fashionme.interactor.ShowJourneyInteractor;
import mx.app.fashionme.pojo.Journey;
import mx.app.fashionme.presenter.ShowJourneyPresenter;
import mx.app.fashionme.presenter.interfaces.IShowJourneyPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IShowJourneyView;

public class ShowJourneyActivity extends AppCompatActivity implements IShowJourneyView {

    private static final String TAG = ShowJourneyActivity.class.getSimpleName();
    @BindView(R.id.action_bar)
    Toolbar toolbar;

    @BindView(R.id.rvJourneyShow)
    RecyclerView rvJourneyShow;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.rootView)
    CoordinatorLayout rootView;

    private IShowJourneyPresenter presenter;
    private Context context;
    private Activity activity;
    private int idJourney;

    private Menu menu;

    private ArrayList<String> checklistSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_journey);

        ButterKnife.bind(this);

        context     = getApplicationContext();
        activity    = ShowJourneyActivity.this;

        Bundle parameters = getIntent().getExtras();
        if (parameters != null) {
            idJourney = parameters.getInt(JourneyListAdapter.ID_JOURNEY, 0);
        }

        setUpToolbar();
        updateColors();
        setUpPresenter();
        getDataJourney();
        presenter.checkFav(idJourney);
        presenter.setAnalytics();
    }

    @Override
    public void setUpPresenter() {
        presenter = new ShowJourneyPresenter( this, new ShowJourneyInteractor(), context);
    }

    @Override
    public void getDataJourney() {
        presenter.getJourney(idJourney);
    }

    @Override
    public void setUpToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }
    }

    @Override
    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void generateLinearLayout() {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvJourneyShow.setLayoutManager(llm);
    }

    @Override
    public JourneyAdapter createAdapter(final Journey journey) {
        menu.add("Checklist").setIcon(R.drawable.ic_logo_2_white).setOnMenuItemClickListener(menuItem -> {
            //presenter.openDialogChecklist(journey, new MaterialDialog.Builder(ShowJourneyActivity.this));
            presenter.openActivityChecklist(journey);
            return true;
        });
        return new JourneyAdapter(journey, activity);
    }

    @Override
    public void initializeAdapter(JourneyAdapter adapter) {
        rvJourneyShow.setAdapter(adapter);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(rootView, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.getJourney(idJourney);
                    }
                })
                .show();
    }

    @Override
    public void setButtonFav(boolean isFav) {
        if (isFav) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite));
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.menu_item_fav) {
                        presenter.removeFav(idJourney);
                    }
                    return false;
                }
            });
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.outline_favorite_border_white_24));
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.menu_item_fav) {
                        presenter.addFav(idJourney);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void favAdded() {
        menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_fav) {
                    presenter.removeFav(idJourney);
                }
                return false;
            }
        });
    }

    @Override
    public void favRemoved() {
        menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.outline_favorite_border_white_24));
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_item_fav) {
                    presenter.addFav(idJourney);
                }
                return false;
            }
        });
    }

    @Override
    public void showDialogChecklist(MaterialDialog.Builder dialog, ArrayList<String> names) {

        checklistSelected = new ArrayList<>();

        dialog.title("Checklist")
                .items(names)
                .itemsCallbackMultiChoice(
                        null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                for (int i = 0; i < which.length; i++) {
                                    checklistSelected.add(text[i].toString());
                                }

                                if (checklistSelected.size() > 0){
                                    presenter.checkListSelected(checklistSelected);
                                }
                                return true;
                            }
                        }
                )
                .onNeutral(((materialDialog, which) -> materialDialog.clearSelectedIndices()))
                .onPositive((materialDialog, which) -> materialDialog.dismiss())
                .positiveText("Guardar")
                .autoDismiss(false)
                .neutralText("Borrar todo")
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.action_menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Message";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_with)));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateColors() {

        List<String> colorPrimaryList = Arrays.asList(getResources().getStringArray(R.array.color_choices));
        List<String> colorPrimaryDarkList = Arrays.asList(getResources().getStringArray(R.array.color_choices_700));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String newColorString = getColorHex(Utils.getTabColor(this));
            getWindow().setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
            getWindow().setNavigationBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
        }
    }

    private String getColorHex(int color) {
        return String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }
}

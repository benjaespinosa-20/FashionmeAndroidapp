package mx.app.fashionme.view;

import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.CharacteristicAdapter;
import mx.app.fashionme.interactor.ChecklistInteractor;
import mx.app.fashionme.pojo.Characteristic;
import mx.app.fashionme.presenter.ChecklistPresenter;
import mx.app.fashionme.presenter.interfaces.IChecklistPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IChecklistView;

import static mx.app.fashionme.adapter.JourneyListAdapter.ID_JOURNEY;

public class ChecklistActivity extends AppCompatActivity implements IChecklistView {

    private static final String TAG = ChecklistActivity.class.getSimpleName();

    @BindView(R.id.action_bar)
    Toolbar toolbarChecklist;

    @BindView(R.id.lvChecks)
    ListView listView;

    @BindView(R.id.btnSend)
    Button btnSendChecklist;

    private IChecklistPresenter presenter;
    private int idJourney;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        ButterKnife.bind(this);
        idJourney   = getIntent().getIntExtra(ID_JOURNEY, 0);

        btnSendChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.saveChecklist(listView, idJourney);
            }
        });

        setToolbarChecklist();
        updateColors();
        setUpPresenterChecklist();
        presenter.getChecklist(idJourney);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.checklist_title)
                .content(R.string.saving)
                .progress(true, 0)
                .progressIndeterminateStyle(true);

        dialog = builder.build();

        presenter.setAnalytics();
    }

    @Override
    public void setAdapterChecklist(ArrayList<Characteristic> checklist) {
        ArrayAdapter<Characteristic> adapter = new CharacteristicAdapter(checklist, this);
        listView.setAdapter(adapter);
    }

    @Override
    public void setToolbarChecklist() {
        if (toolbarChecklist != null) {
            setSupportActionBar(toolbarChecklist);
            toolbarChecklist.setBackgroundColor(Utils.getTabColor(this));
        }
    }

    @Override
    public void setUpPresenterChecklist() {
        presenter = new ChecklistPresenter(this, new ChecklistInteractor(), getApplicationContext());

    }

    @Override
    public void showError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog(boolean show) {
        if (show){
         dialog.show();
        } else {
            dialog.dismiss();
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

package mx.app.fashionme.view;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.CharacteristicAdapter;
import mx.app.fashionme.interactor.CharacteristicInteractor;
import mx.app.fashionme.pojo.Characteristic;
import mx.app.fashionme.presenter.CharacteristicPresenter;
import mx.app.fashionme.presenter.interfaces.ICharacteristicPresenter;
import mx.app.fashionme.ui.modules.home.controller.HomeController;
import mx.app.fashionme.view.interfaces.ICharacteristicView;

public class CharacteristicActivity extends AppCompatActivity implements ICharacteristicView {

    public static final String TAG = CharacteristicActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ListView listView;
    private Button btnEnviar;
    private ICharacteristicPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic);

        toolbar = findViewById(R.id.action_bar_characteristic);
        listView = findViewById(R.id.rvCharacteristic);
        btnEnviar = findViewById(R.id.btnSendCharacteristics);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCharacteristics();
            }
        });

        setToolbarCharacteristic();
        setUpPresenter();
        presenter.getData();
    }

    @Override
    public void setAdapterCharacteristic(ArrayList<Characteristic> characteristics) {
        ArrayAdapter<Characteristic> adapter = new CharacteristicAdapter(characteristics, this);
        listView.setAdapter(adapter);
    }

    @Override
    public void setToolbarCharacteristic() {
        setSupportActionBar(toolbar);
    }

    @Override
    public void setUpPresenter() {
        presenter = new CharacteristicPresenter(this, new CharacteristicInteractor(), getApplicationContext(), this);
    }

    @Override
    public void getError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void sendCharacteristics() {
        presenter.sendData(listView);
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, HomeController.class));
        this.finish();
    }

    @Override
    public void alertBody() {
        //Toast.makeText(this, "Espera mientras se calcula tu cuerpo", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, BodyActivity.class));
        this.finish();
    }


}

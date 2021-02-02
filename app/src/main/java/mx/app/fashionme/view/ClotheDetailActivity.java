package mx.app.fashionme.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import mx.app.fashionme.R;
import mx.app.fashionme.interactor.ClotheDetailInteractor;
import mx.app.fashionme.presenter.ClotheDetailPresenter;
import mx.app.fashionme.presenter.interfaces.IClotheDetailPresenter;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IClotheDetailView;


public class ClotheDetailActivity extends AppCompatActivity implements IClotheDetailView, View.OnClickListener {

    public static final String TAG = ClotheDetailActivity.class.getSimpleName();

    public static final String URL_IMAGE = "IMAGE_URL";
    public static final String CLOTHE_ID = "CLOTHE_ID";
    private Toolbar toolbar;

    private PhotoView ivClotheImage;
    private TextView tvCloteName;
    private TextView tvClotheBrand;
    private TextView tvPrice;
    private FloatingActionButton fab_cart;
    private FloatingActionButton fab_fav;

    private String url;
    private String link;
    private int idClothe;
    private String name;

    private IClotheDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothe_detail);

        toolbar = findViewById(R.id.action_bar_clothe_detail);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }

        updateColors();

        Bundle parameters = getIntent().getExtras();
        presenter = new ClotheDetailPresenter(this, getApplicationContext(), parameters, ClotheDetailActivity.this, new ClotheDetailInteractor());
        presenter.checkFav(idClothe);
        presenter.checkShopping(idClothe);
        presenter.setAnalytics();

    }

    @Override
    public void initializeViews() {
        ivClotheImage   = findViewById(R.id.ivClotheImage);
        tvCloteName     = findViewById(R.id.tvClotheName);
        tvClotheBrand   = findViewById(R.id.tvClotheBrand);
        tvPrice         = findViewById(R.id.tvClothePrice);
        fab_cart        = findViewById(R.id.fab_cart);
        fab_fav         = findViewById(R.id.fab_favorite);
    }

    @Override
    public void setViews(String urlImage, String name, String brand, String price, int id, String link) {

        Picasso.get()
                .load(urlImage)
                .into(ivClotheImage);
        //Picasso.get().load(urlImage).placeholder(R.drawable.logo_fashion).into(ivClotheImage);
        tvCloteName.setText(name);
        tvClotheBrand.setText(getResources().getString(R.string.clothe_detail_brand, brand));
        tvPrice.setText(getResources().getString(R.string.clothe_detail_price, price));
        idClothe = id;
        this.name = name;
        this.link = link;
        url = urlImage;
    }

    @Override
    public void setFabFav(boolean isFav) {
        if (isFav) {
            fab_fav.setIcon(R.drawable.ic_favorite);
            fab_fav.setTitle("Quitar de favoritos");
            fab_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.removeFav(idClothe);
                }
            });
        } else {
            fab_fav.setIcon(R.drawable.outline_favorite_border_white_24);
            fab_fav.setTitle("Agregar a favoritos");
            fab_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.addClotheToFav(idClothe);
                }
            });
        }
    }

    @Override
    public void setFabShopping(boolean isInCar) {
        if (isInCar) {
            fab_cart.setIcon(R.drawable.ic_shopping_cart);
            fab_cart.setTitle("Sacar de compras");
            fab_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.removeShopping(idClothe);

                }
            });
        } else {
            fab_cart.setIcon(R.drawable.outline_shopping_cart_white_24);
            fab_cart.setTitle("Agregar a compras");
            fab_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.addToShopping(idClothe, name, name, link, url);
                }
            });
        }
    }

    @Override
    public void favAdded() {
        fab_fav.setIcon(R.drawable.ic_favorite);
        fab_fav.setTitle("Quitar de favoritos");
        fab_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.removeFav(idClothe);
            }
        });
    }

    @Override
    public void favRemoved() {
        fab_fav.setIcon(R.drawable.outline_favorite_border_white_24);
        fab_fav.setTitle("Agregar a favoritos");
        fab_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addClotheToFav(idClothe);
            }
        });
    }

    @Override
    public void shoppingAdded() {
        fab_cart.setIcon(R.drawable.ic_shopping_cart);
        fab_cart.setTitle("Sacar de compras");
        fab_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.removeShopping(idClothe);
            }
        });
    }

    @Override
    public void shoppingRemoved() {
        fab_cart.setIcon(R.drawable.outline_shopping_cart_white_24);
        fab_fav.setTitle("Agregar a compras");
        fab_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addToShopping(idClothe, name, name, link, url);
            }
        });
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()){

            case R.id.mCreateLook:

                intent = new Intent( getApplicationContext(), MakeLookActivity.class);
                intent.putExtra(URL_IMAGE, url);
                intent.putExtra(CLOTHE_ID, idClothe);
                startActivity(intent);
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClotheImage:
                //presenter.showZoom(ClotheDetailActivity.this, url);
                break;
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

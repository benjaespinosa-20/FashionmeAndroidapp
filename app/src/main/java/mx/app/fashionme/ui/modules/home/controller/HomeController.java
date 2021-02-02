package mx.app.fashionme.ui.modules.home.controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;

import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.app.fashionme.R;
import mx.app.fashionme.ui.modules.acquire.fragments.AcquireFragment;
import mx.app.fashionme.settings.OtherSettingsActivity;
import mx.app.fashionme.adapter.HomeItemAdapter;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.ui.app.config.BillingAgentConfig;
import mx.app.fashionme.ui.modules.commons.BaseController;
import mx.app.fashionme.ui.modules.home.contracts.HomeContracts;
import mx.app.fashionme.ui.modules.home.enums.HomeEnum;
import mx.app.fashionme.ui.utils.NavigationUtils;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.utils.HomeItems;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.AssessorContactsActivity;
import mx.app.fashionme.view.BodyActivity;
import mx.app.fashionme.view.CalendarActivity;
import mx.app.fashionme.view.CategoryActivity;
import mx.app.fashionme.view.CharacteristicActivity;
import mx.app.fashionme.view.ChatActivity;
import mx.app.fashionme.view.ChatAssessorActivity;
import mx.app.fashionme.view.ColorTestActivity;
import mx.app.fashionme.view.FaceActivity;
import mx.app.fashionme.view.FavoriteActivity;
import mx.app.fashionme.view.JourneyActivity;
import mx.app.fashionme.view.LooksActivity;
import mx.app.fashionme.view.MakeLookActivity;
import mx.app.fashionme.view.MakeupActivity;
import mx.app.fashionme.view.RecommendationActivity;
import mx.app.fashionme.view.ResultActivity;
import mx.app.fashionme.view.ShoppingActivity;
import mx.app.fashionme.view.StartActivity;
import mx.app.fashionme.view.StyleTestActivity;
import mx.app.fashionme.view.TipActivity;
import mx.app.fashionme.view.TrendActivity;
import mx.app.fashionme.view.UserProfileActivity;
import mx.app.fashionme.view.VisageActivity;
import mx.app.fashionme.view.WayDressingActivity;
import mx.app.fashionme.view.WeatherActivity;

public class HomeController extends BaseController implements HomeContracts.IHomeView, BillingAgentConfig.BillingCallback, HomeItemAdapter.ClickModuleListener {

    private TextView tvNombre;
    private TextView tvNavUserName;
    private TextView tvEmailProfile;
    private CircleImageView imgCircleProfile;
    private LinearLayout root_nav_header;

    private RecyclerView rvHomeItems;
    private RecyclerView rvHomeItemsRecommend;
    private Toolbar toolbar;

    private LinearLayout llMain;
    private LinearLayout llProgress;

    private DrawerLayout rootView;
    private CoordinatorLayout coordinatorView;
    private NavigationView navigationView;

    private Button btnWebPage;
    private Button btnInstagram;
    private TextView tvVersionName;

    private GridLayoutManager glm;

    /**
     * Instancia de presenter
     */
    @Inject
    public HomeContracts.IHomePresenter presenter;

    private AcquireFragment mAcquireFragment;
    private static final String DIALOG_TAG = "dialog";

    @Inject
    BillingAgentConfig mBillingAgentConfig;

    private int mCurrentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvNombre                = findViewById(R.id.tvUsername);
        rvHomeItems             = findViewById(R.id.rvHomeItems);
        rvHomeItemsRecommend    = findViewById(R.id.rvHomeItemsRecommend);

        llMain                  = findViewById(R.id.llHome);
        llProgress              = findViewById(R.id.llHomeProgress);

        rootView                = findViewById(R.id.rootViewHome);
        coordinatorView         = findViewById(R.id.mainContentHome);
        navigationView          = findViewById(R.id.nav_view_home);

        View hView              = navigationView.getHeaderView(0);
        tvNavUserName           = hView.findViewById(R.id.tvNavUsername);
        tvEmailProfile          = hView.findViewById(R.id.tvEmailProfile);
        imgCircleProfile        = hView.findViewById(R.id.imgCircleProfile);
        root_nav_header         = hView.findViewById(R.id.root_nav_header);

        toolbar                 = findViewById(R.id.toolbarHome);

        setUpToolbar();
        setUpNavigationView();
        setUpAnalytics();

        toolbar.setBackgroundColor(Utils.getTabColor(getApplicationContext()));
        root_nav_header.setBackgroundColor(Utils.getTabColor(getApplicationContext()));
        updateStatusBarColor();
        mBillingAgentConfig.initConfig(this);
        mCurrentId = -1;
    }

    private void setUpAnalytics() {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        Bundle params = new Bundle();
        params.putString("screen_name", "Inicio/Android");
        firebaseAnalytics.logEvent("open_screen", params);
    }

    private void updateStatusBarColor() {
        List<String> colorPrimaryList = Arrays.asList(getApplicationContext().getResources().getStringArray(R.array.color_choices));
        List<String> colorPrimaryDarkList = Arrays.asList(getApplicationContext().getResources().getStringArray(R.array.color_choices_700));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String newColorString = getColorHex(Utils.getTabColor(getApplicationContext()));
            getWindow().setStatusBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
            getWindow().setNavigationBarColor((Color.parseColor(colorPrimaryDarkList.get(colorPrimaryList.indexOf(newColorString)))));
        }
    }

    private String getColorHex(int color) {
        return String.format("#%02x%02x%02x", Color.red(color), Color.green(color), Color.blue(color));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.checkConditions(getApplicationContext());
    }

    @Override
    public void setUpToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                // Poner ícono del drawer toggle
                ab.setHomeAsUpIndicator(R.drawable.ic_menu);
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void setUpNavigationView() {
        if (navigationView != null) {
            // Añadir carácteristicas
            setupDrawerContent(navigationView);
        }
    }

    @Override
    public void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Marcar item presionado
                        menuItem.setCheckable(false);
                        selectItem(menuItem);
                        return true;
                    }
                }
        );
    }

    @Override
    public void selectItem(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_log_out:
                logout();
                break;
            case R.id.nav_profile:
                goToProfilePage();
                break;
            case R.id.nav_looks:
                goToLooksPage();
                break;
            case R.id.nav_chat:
                goToChat();
                break;
            case R.id.nav_buys:
                goToShopping();
                break;
            case R.id.nav_about:
                presenter.openDialogAbout(new Dialog(HomeController.this));
                break;
            case R.id.nav_favs:
                goToFavs();
                break;
            case R.id.nav_settings:
                goToSettingsPage();
                break;
            case R.id.nav_asesor:
                if (SessionPrefs.get(getApplicationContext()).getRole() != null && SessionPrefs.get(getApplicationContext()).getRole().toLowerCase().equals("assessor")) {
                    startActivity(new Intent(getApplicationContext(), AssessorContactsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else if (SessionPrefs.get(getApplicationContext()).getRole() != null) {
                    presenter.chatWithConsultant();
                } else {
                    onError(getApplicationContext().getString(R.string.not_open_activity_assessor_chat));
                }
                break;
            case R.id.nav_help:
                String email = "fashionmeblg@gmail.com";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ayuda");
                startActivity(Intent.createChooser(emailIntent,  "Enviar email"));
                break;
        }
        rootView.closeDrawers(); // Cerrar drawer
    }

    private void goToFavs() {
        Intent intent = new Intent(HomeController.this, FavoriteActivity.class);
        startActivity(intent);
    }

    private void goToShopping() {
        Intent intent = new Intent(HomeController.this, ShoppingActivity.class);
        startActivity(intent);
    }

    private void goToChat() {
        startActivity(new Intent(this, ChatActivity.class));
    }

    @Override
    public void goToLoginPage() {
        startActivity(new Intent(this, StartActivity.class));
        this.finish();
    }

    @Override
    public void goToTakePhotoPage() {
        startActivity(new Intent(this, BodyActivity.class));
        this.finish();
    }

    @Override
    public void goToCharacteristicsPage() {
        startActivity(new Intent(this, CharacteristicActivity.class));
        this.finish();
    }

    @Override
    public void goToTestPage() {
        startActivity(new Intent(this, ColorTestActivity.class));
        this.finish();
    }

    @Override
    public void goToResultPage() {
        startActivity(new Intent(this, ResultActivity.class));
        this.finish();
    }

    @Override
    public void goToProfilePage() {
        startActivity(new Intent(this, UserProfileActivity.class));
    }

    @Override
    public void goToLooksPage() {
        startActivity(new Intent(this, LooksActivity.class));
    }

    @Override
    public void goToSettingsPage() {
        startActivity(new Intent(this, OtherSettingsActivity.class));

    }

    @Override
    public void generateGridLayout(int columns) {
        glm = new GridLayoutManager(getApplicationContext(), 3);
        rvHomeItems.setLayoutManager(glm);
    }

    @Override
    public HomeItemAdapter createAdapterItems(boolean trial, boolean premium) {
        return new HomeItemAdapter(
                getApplicationContext(),
                HomeItems.listItemHome(getApplicationContext()),
                premium,
                this
        );
    }

    @Override
    public void initializeAdapterItems(HomeItemAdapter adapter) {
        rvHomeItems.setAdapter(adapter);
    }

    @Override
    public void generateGridLayoutItemsRecommend(int columns) {
        glm = new GridLayoutManager(getApplicationContext(), columns);
        rvHomeItemsRecommend.setLayoutManager(glm);
    }

    @Override
    public HomeItemAdapter createAdapterItemsRecommend(boolean trial, boolean premium) {
        return new HomeItemAdapter(
                getApplicationContext(),
                HomeItems.listItemRecommend(getApplicationContext()),
                premium,
                this
        );
    }

    @Override
    public void initializeAdapterItemsRecommend(HomeItemAdapter adapter) {
        rvHomeItemsRecommend.setAdapter(adapter);
    }

    @Override
    public void setUsername(String username, String email, String photoUri) {
        tvNombre.setText(getString(R.string.hello_username, username));
        tvNavUserName.setText(username);
        tvEmailProfile.setText(email);

        if (photoUri != null){
            imgCircleProfile.setImageURI(Uri.parse(photoUri));
        }
    }

    @Override
    public void showLoading(boolean show) {
        llProgress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        llMain.setVisibility(show ? View.INVISIBLE: View.VISIBLE);
    }

    @Override
    public void showSnackBar(String msg) {
        Snackbar.make(coordinatorView, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.checkConditions(getApplicationContext());
                    }
                })
                .show();
    }

    @Override
    public void logout() {
        SessionPrefs.get(getApplicationContext()).logOut(this);
        startActivity(new Intent(this, HomeController.class));
        this.finish();
    }

    @Override
    public void onCreateDialogAbout(Dialog dialog) {
        btnWebPage      = dialog.findViewById(R.id.btnWebPage);
        btnInstagram    = dialog.findViewById(R.id.btnInstagram);
        tvVersionName   = dialog.findViewById(R.id.tvVersion);

        presenter.getVersionName(getApplicationContext());

        btnWebPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://fashionmeweb.com"));
                startActivity(browserIntent);
            }
        });

        btnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/fashionmeblg");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
            }
        });
    }

    @Override
    public void showDialog(Dialog dialog) {
        dialog.show();
    }

    @Override
    public void setVersionName(String version) {
        tvVersionName.setText(getString(R.string.version_app, version));
    }

    @Override
    public void showDialogPremium(String message, String title) {
        new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText("Continuar")
                .negativeText("Cancelar")
                .positiveColorRes(R.color.colorAccent)
                .titleGravity(GravityEnum.CENTER)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(android.R.color.white)
                .backgroundColorRes(R.color.material_blue_grey_800)
                .dividerColorRes(R.color.colorAccent)
                .btnSelector(R.drawable.md_btn_selector_custom, DialogAction.POSITIVE)
                .positiveColor(Color.WHITE)
                .negativeColorAttr(android.R.attr.textColorSecondaryInverse)
                .theme(Theme.DARK)
                .onPositive((dialog, which) -> {
                    if (mAcquireFragment == null) {
                        mAcquireFragment = new AcquireFragment();
                    }

                    if (!isAcquireFragmentShown()) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.TYPE, HomeEnum.SUBS.name());
                        mAcquireFragment.setArguments(bundle);
                        mAcquireFragment.show(getSupportFragmentManager(), DIALOG_TAG);
                    }
                })
                .show();
    }

    @Override
    public void showDialogChat(String message, String title) {
        new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText("Continuar")
                .negativeText("Cancelar")
                .positiveColorRes(R.color.colorAccent)
                .titleGravity(GravityEnum.CENTER)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(android.R.color.white)
                .backgroundColorRes(R.color.material_blue_grey_800)
                .dividerColorRes(R.color.colorAccent)
                .btnSelector(R.drawable.md_btn_selector_custom, DialogAction.POSITIVE)
                .positiveColor(Color.WHITE)
                .negativeColorAttr(android.R.attr.textColorSecondaryInverse)
                .theme(Theme.DARK)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (mAcquireFragment == null) {
                            mAcquireFragment = new AcquireFragment();
                        }

                        if (!isAcquireFragmentShown()) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.TYPE, HomeEnum.INAPP.name());
                            mAcquireFragment.setArguments(bundle);
                            mAcquireFragment.show(getSupportFragmentManager(), DIALOG_TAG);
                        }
                    }
                })
                .show();

    }

    /**
     * Metodo de vista cuando el tiempo de chat ha finalizado
     */
    @Override
    public void onPendingChatTimeFinished() {
        showDialogChat("Para continuar necesitas comprar tiempo de asesor", "Comprar chat");
    }

    /**
     * Metodo de vista cuando el tiempo de chat
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onPendingChatTime() {
        goNextView(ChatAssessorActivity.class);
    }

    /**
     * Metodo para mostrar dialogo de suscripcion
     */
    @Override
    public void showSubscriptionDialog() {
        int messageId = -1;
        switch (mCurrentId) {
            case 0:
                messageId = R.string.premium_description_ideal_closet;
                break;
            case 2:
                messageId = R.string.premium_description_style;
                break;
            case 130:
                messageId = R.string.premium_description_journey;
                break;
        }
        showDialogPremium(getApplicationContext().getString(messageId), getString(R.string.premium_subscription_title));
    }

    /**
     * Metodo para abrir el modulo con suscripcion activa
     */
    @Override
    @SuppressWarnings("unchecked")
    public void openModuleWithSubscriptionActive() {
        switch (mCurrentId) {
            case 0:
                goNextView(CategoryActivity.class);
                break;
            case 2:
                goNextView(StyleTestActivity.class);
                break;
            case 130:
                goNextView(JourneyActivity.class);
                break;
        }
    }

    private boolean isAcquireFragmentShown() {
        return mAcquireFragment != null && mAcquireFragment.isVisible();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!rootView.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.home_menu, menu);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                rootView.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_item_weather:
                startActivity(new Intent(HomeController.this, WeatherActivity.class));
                //invalidateOptionsMenu();
                return true;
            case R.id.menu_item_calendar:
                startActivity(new Intent(HomeController.this, CalendarActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTokenConsumed(List<Purchase> purchases) {
        new MaterialDialog.Builder(HomeController.this)
                .title("¡Felicidades!")
                .content("Compra realizada con exito")
                .positiveText("Volver")
                .positiveColorRes(R.color.colorAccent)
                .titleGravity(GravityEnum.CENTER)
                .titleColorRes(R.color.colorAccent)
                .contentColorRes(android.R.color.white)
                .backgroundColorRes(R.color.material_blue_grey_800)
                .dividerColorRes(R.color.colorAccent)
                .btnSelector(R.drawable.md_btn_selector_custom, DialogAction.POSITIVE)
                .positiveColor(Color.WHITE)
                .negativeColorAttr(android.R.attr.textColorSecondaryInverse)
                .theme(Theme.DARK)
                .onPositive((dialog, which) -> {
                    dialog.dismiss();
                    mAcquireFragment.dismiss();
                })
                .show();
    }

    /**
     * Metodo para manejar click de elemento correspondiente
     * @param idItem id del elemento
     * @param isPremium Bandera para saber si el usuario es premium
     */
    @Override
    @SuppressWarnings("unchecked")
    public void onClickModule(int idItem, boolean isPremium) {
        //int purchases = mBillingAgentConfig.queryPurchases();
        mCurrentId = idItem;
        switch (idItem) {
            // Closet ideal
            // BLOQEADA
            case 0:
                SessionPrefs.get(getApplicationContext()).isIdealCloset(true);
                SessionPrefs.get(getApplicationContext()).saveActivityAfterResult(Constants.CATEGORY);
                presenter.getSubscription(isPremium);
                break;
            // Closet FME
            case 1:
                SessionPrefs.get(getApplicationContext()).isIdealCloset(false);
                goNextView(CategoryActivity.class);
                break;
            // Tu estilo
            // BLOQUEADA
            case 2:
            // Viajes
            // BLOQUEADA
            case 130:
                presenter.getSubscription(isPremium);
                break;
            // Recomendacion
            case 4:
                SessionPrefs.get(getApplicationContext()).saveActivityAfterResult(Constants.RECOMMENDATIONS);
                goNextView(RecommendationActivity.class);
                break;
            // Crear look gral.
            case 5:
                goNextView(MakeLookActivity.class);
                break;
            // Visajismo
            case 6:
                goNextView(VisageActivity.class);
                break;
            // Cara
            case 7:
                goNextView(FaceActivity.class);
                break;
            // Maquillaje
            case 8:
                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.KEY_MH, Constants.VALUE_MH_M);
                goNextView(MakeupActivity.class, params);
                break;
            // Cabello
            case 9:
                HashMap<String, String> paramsHair = new HashMap<>();
                paramsHair.put(Constants.KEY_MH, Constants.VALUE_MH_H);
                goNextView(MakeupActivity.class, paramsHair);
                break;
            // Tendencias
            case 100:
                goNextView(TrendActivity.class);
                break;
            // Código de vestimenta
            case 110:
                goNextView(WayDressingActivity.class);
                break;
            // Tips
            case 120:
                goNextView(TipActivity.class);
                break;
        }
    }

    /**
     * Metodo para manejar la navegación a la siguiente pantalla
     * @param _class Nombre de clase destino
     * @param params Parametros de intent
     */
    private void goNextView(Class<?> _class, HashMap<String, String>...params) {
        NavigationUtils.Builder builder = new NavigationUtils.Builder(this, _class);
        if (params.length > 0) {
            builder.setParams(params[0]);
        }
        builder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        builder.start();
    }

    /**
     * Metodo para mostrar progress bar
     */
    @Override
    public void showProgress() {
        llProgress.setVisibility(View.VISIBLE);
        llMain.setVisibility(View.INVISIBLE);
    }

    /**
     * Metodo para ocultar progress bar
     */
    @Override
    public void hideProgress() {
        llProgress.setVisibility(View.INVISIBLE);
        llMain.setVisibility(View.VISIBLE);
    }

    /**
     * Metodo para mostrar mensajes de error
     */
    @Override
    public void onError(String error) {
        Snackbar.make(coordinatorView, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.try_again, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.checkConditions(getApplicationContext());
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        mBillingAgentConfig.onDestroy();
        mCurrentId = -1;
        super.onDestroy();
    }
}

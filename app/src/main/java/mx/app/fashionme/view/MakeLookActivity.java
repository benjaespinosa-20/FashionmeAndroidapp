package mx.app.fashionme.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.SuggestionAdapter;
import mx.app.fashionme.interactor.MakeLookInteractor;
import mx.app.fashionme.pojo.Clothe;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.pojo.Subcategory;
import mx.app.fashionme.presenter.MakeLookPresenter;
import mx.app.fashionme.presenter.interfaces.IMakeLookPresenter;
import mx.app.fashionme.sticker.StickerImageView;
import mx.app.fashionme.sticker.StickerTextInputDialog;
import mx.app.fashionme.sticker.StickerTextView;
import mx.app.fashionme.sticker.StickerView;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IMakeLookView;

public class MakeLookActivity extends AppCompatActivity implements IMakeLookView {

    private static final String TAG = MakeLookActivity.class.getSimpleName();

    private Toolbar toolbar;

    private FrameLayout canvas;
    private CoordinatorLayout rootView;
    private LinearLayout bottomSheet;

    private BottomSheetBehavior sheetBehavior;

    private RecyclerView rvSuggestions;
    private GridLayoutManager glm;

    private int clotheId;

    private StickerTextInputDialog stickerTextInputDialog;
    private StickerTextView stickerTextView;
    private ArrayList<StickerView> stickers;

    private Button btnFilter;
    private List<String> listItems;

    private IMakeLookPresenter presenter;

    private SuggestionAdapter adapter;
    private String filteredCategory;

    private ArrayList<Clothe> suggestionsList;
    private ArrayList<Clothe> currentSuggestion;

    private ImageView ivArrow;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_look);

        canvas = findViewById(R.id.contentCanvasView);
        rootView = findViewById(R.id.rootView);
        bottomSheet = findViewById(R.id.bottomSheet);

        rvSuggestions = findViewById(R.id.rvBottomSheet);

        toolbar = findViewById(R.id.toolbarMakeLook);

        btnFilter = findViewById(R.id.btnFilter);

        ivArrow = findViewById(R.id.ivArrow);

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        stickers = new ArrayList<>();

        setUpPresenterMakeLook();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }

        updateColors();

        final String urlImg = getIntent().getStringExtra(ClotheDetailActivity.URL_IMAGE);
        // add a stickerImage to canvas if the last activity was ClotheDetail
        if (urlImg != null) {
            addImageSelected(urlImg);
        }

        clotheId            = getIntent().getIntExtra(ClotheDetailActivity.CLOTHE_ID,0);

        if (clotheId != 0){
            getSuggestionsByClotheId(clotheId);
        } else {
            getSuggestionsGeneral();
        }


        stickerTextInputDialog = new StickerTextInputDialog(this);

        listItems = new ArrayList<>();
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        ivArrow.setImageResource(R.drawable.baseline_keyboard_arrow_down_black_48);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        ivArrow.setImageResource(R.drawable.baseline_keyboard_arrow_up_black_48);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ivArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        checkPermissions();
        presenter.setAnalytics();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void addImageSelected(final String urlImg) {
        final StickerImageView iv_sticker = new StickerImageView(getApplicationContext(), this);

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlImg);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    return myBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                iv_sticker.setImageBitmap(bitmap);
                super.onPostExecute(bitmap);
            }
        }.execute();

        stickers.add(iv_sticker);
        iv_sticker.setId(stickers.size());
        canvas.addView(iv_sticker);
    }

    @Override
    public void generateGridLayout(int columns) {
        glm = new GridLayoutManager(getApplicationContext(), columns);
        rvSuggestions.setLayoutManager(glm);
    }

    @Override
    public SuggestionAdapter createAdapterSuggestClothe(ArrayList<Clothe> clothes) {
        return new SuggestionAdapter(clothes, this, getApplicationContext(), this);
    }

    @Override
    public void initializeAdapterSuggestions(SuggestionAdapter adapter) {
        this.adapter = adapter;
        rvSuggestions.setAdapter(adapter);
    }

    @Override
    public void setListSubcategories(ArrayList<Subcategory> subcategories) {

        String lang = getResources().getString(R.string.app_language);

        switch (lang) {
            case "spanish":
                for (Subcategory subcategory : subcategories) {
                    listItems.add(subcategory.getSpanish().getName());
                }
                break;
            case "english":
                for (Subcategory subcategory : subcategories) {
                    listItems.add(subcategory.getEnglish().getName());
                }
                break;
            default:
                for (Subcategory subcategory : subcategories) {
                    listItems.add(subcategory.getSpanish().getName());
                }
                break;
        }

        //Creamos un objeto HashSet

        //Lo cargamos con los valores del array, esto hace quite los repetidos
        HashSet hs = new HashSet(listItems);

        //Limpiamos el array
        listItems.clear();

        //Agregamos los valores sin repetir
        listItems.addAll(hs);
    }

    @Override
    public void setUpPresenterMakeLook() {
        presenter = new MakeLookPresenter(this, new MakeLookInteractor(), getApplicationContext());
    }

    @Override
    public void getSuggestionsByClotheId(int id) {
        presenter.getSuggestionsByIdClothe(id);
    }

    @Override
    public void getSuggestionsGeneral() {
        presenter.getSuggestionsGeneral();
    }

    @Override
    public void showError(String err) {
        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setStickerImage(String urlImage) {
        presenter.setStickerSuggestion(urlImage);
    }

    @Override
    public void addImageToBoard(StickerImageView imageView) {
        stickers.add(imageView);
        imageView.setId(stickers.size());
        canvas.addView(imageView);
    }

    @Override
    public void setCurrentSuggestions(ArrayList<Clothe> clothes) {
        this.suggestionsList = clothes;
        this.currentSuggestion = new ArrayList<>();
        currentSuggestion.addAll(clothes);
    }

    @Override
    public void setStickerText(String text) {
        presenter.setStickerText(text, stickerTextInputDialog);
    }

    @Override
    public void addStickerTextToBoard(StickerTextView stickerTextView) {
        this.stickerTextView = stickerTextView;

        stickerTextInputDialog.setCompleteCallBack(new StickerTextInputDialog.CompleteCallBack() {
            @Override
            public void onComplete(View stickerTextView, String str) {
//                ((StickerTextView) stickerTextView).setText(str);
                MakeLookActivity.this.stickerTextView.setText(str);
            }
        });

        stickers.add(stickerTextView);
        stickerTextView.setId(stickers.size());
        canvas.addView(stickerTextView);
    }

    @Override
    public void stickerInEdit(int id) {
        for (StickerView currentStickerView : stickers) {
            if (id != currentStickerView.getId()) {
                currentStickerView.setControlItemsHidden(true);
            } else {
                currentStickerView.setControlItemsHidden(false);
            }
        }
    }

    @Override
    public void showFilterDialog() {

        new AlertDialog.Builder(MakeLookActivity.this)
                .setTitle("Selecciona una subcategoría")
                .setSingleChoiceItems(listItems.toArray(new String[listItems.size()]), -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filteredCategory = listItems.get(which);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (filteredCategory != null) {
                            updateSuggestions(filteredCategory);
                        }
                        filteredCategory = null;
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void updateSuggestions(String select) {
        presenter.updateSuggestions(currentSuggestion, select);
    }

    @Override
    public void updateAdapterSuggestions(ArrayList<Clothe> clothesSort) {
        suggestionsList.clear();
        suggestionsList.addAll(currentSuggestion);
        adapter.notifyDataSetChanged();

        suggestionsList.clear();
        suggestionsList.addAll(clothesSort);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showSnackbar(boolean button, String text) {
        Snackbar snackbar = Snackbar.make(rootView,text,Snackbar.LENGTH_LONG);
        if (button){
            snackbar.setAction(R.string.show_looks, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MakeLookActivity.this, LooksActivity.class));
                    MakeLookActivity.this.finish();
                }
            });
        }
        snackbar.show();
    }

    @Override
    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
        }
    }

    @Override
    public void hideSheetBottom() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void deletedSticker(StickerView stickerView) {
        stickers.remove(stickerView);
    }

    @Override
    public void showCalendar(final Look look) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String format = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                presenter.saveDateLook(look, sdf.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(MakeLookActivity.this, dateListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void addShareMenu(final String urlPathLook) {
        this.menu.add("Compartir");
        menu.getItem(0).setIcon(R.drawable.ic_share);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getOrder() == 0) {
                    //Toast.makeText(MakeLookActivity.this, "Share" + urlPathLook , Toast.LENGTH_SHORT).show();
                    final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/jpg");
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(urlPathLook));
                    } else{
                        final File photoFile = new File(urlPathLook);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
                    }

                    startActivity(Intent.createChooser(shareIntent, "Share image using"));
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.options_make_look, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_text:
                setStickerText(getResources().getString(R.string.edit_text));
                return true;
            case R.id.menu_item_save:
                for (StickerView currentStickerView : stickers) {
                    currentStickerView.setControlItemsHidden(true);
                }
                presenter.saveLook(canvas, stickers);
                return true;
            case R.id.menu_item_add_calendar:
                for (StickerView currentStickerView : stickers) {
                    currentStickerView.setControlItemsHidden(true);
                }
                presenter.addToCalendar(canvas, stickers);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

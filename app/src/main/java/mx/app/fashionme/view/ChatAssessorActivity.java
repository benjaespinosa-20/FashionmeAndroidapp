package mx.app.fashionme.view;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.ChatFirebaseAdapter;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.pojo.FashionMessage;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.presenter.ChatsPresenter;
import mx.app.fashionme.presenter.interfaces.IChatAssessorPresenter;
import mx.app.fashionme.ui.modules.commons.BaseController;
import mx.app.fashionme.ui.modules.home.controller.HomeController;
import mx.app.fashionme.utils.Constants;
import mx.app.fashionme.utils.Utils;
import mx.app.fashionme.view.interfaces.IChatAssessorView;

import static mx.app.fashionme.utils.Constants.CLIENT_EMAIL;

public class ChatAssessorActivity extends BaseController implements IChatAssessorView {

    public static final String TAG = "ChatAssessor";
    public static final int MSG_LENGTH_LIMIT = 250;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.messageRecyclerView)
    RecyclerView rvMessages;

    @BindView(R.id.messageEditText)
    EditText etMessage;

    @BindView(R.id.addMessageImageView)
    ImageView addMessageImage;

    @BindView(R.id.sendButton)
    Button sendButton;

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @BindView(R.id.tvCountTime)
    TextView count;

    @Inject
    IChatAssessorPresenter presenter;

    private Snackbar snackBar;
    private ChatAssessorClient chat;

    // Firebase instance variables
    private DatabaseReference firebaseInstance;
    private ChatFirebaseAdapter chatFirebaseAdapter;
    private LinearLayoutManager llm;
    private String key;

    /**
     * Tiempo resatante
     */
    private long mTimeUntilFinished;
    /**
     * Timer
     */
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_assessor);

        ButterKnife.bind(this);

        setupToolbar();
        updateColors();

        // Get intent
        setChat(getIntent().getParcelableExtra(ChatsPresenter.KEY_FROM_ASSESSOR));

        //presenter = new ChatAssessorPresenter(this, new ChatAssessorInteractor(), getApplicationContext(), this);

        if (!SessionPrefs.get(this).isLoggedIn()) {
            // Not signed in, launch the Sign in activity
            startActivity(new Intent(this, HomeController.class));
            finish();
        }
        presenter.setView(this);
        presenter.instantiateFirebase();
        presenter.initializeChat();

        snackBar = Snackbar.make(linearLayout, R.string.wait_assessor_message, Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction(R.string.exit, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatAssessorActivity.this.finish();
            }
        });

        etMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MSG_LENGTH_LIMIT)});
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FashionMessage message = new FashionMessage(
                        etMessage.getText().toString(),
                        presenter.getUsername(),
                        null,
                        null,
                        presenter.getCurrentDate(),
                        presenter.getEmailUser()
                );
                presenter.sendMessage(message);
                etMessage.setText("");
            }
        });

        addMessageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, Constants.REQUES_IMAGE_OPENABLE);
            }
        });

        if (chatFirebaseAdapter != null && chatFirebaseAdapter.getItemCount() == 0) {
            progressBar.setVisibility(View.GONE);
        }

        presenter.setAnalytics();
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayHomeAsUpEnabled(true);
            }

            toolbar.setBackgroundColor(Utils.getTabColor(this));
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

    @Override
    public void registerNewChat() {
        presenter.registerChat(firebaseInstance);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLinearLayoutChat() {
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLinearLayoutChat() {
        linearLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public DatabaseReference getFirebaseInstance() {
        return this.firebaseInstance;
    }

    @Override
    public void setFirebaseInstance(DatabaseReference firebaseInstance) {
        this.firebaseInstance = firebaseInstance;
    }

    @Override
    public String getKeyUID() {
        return this.key;
    }

    @Override
    public void setKeyUID(String keyUID) {
        this.key = keyUID;
        presenter.addFirebaseListener(firebaseInstance, key);
    }

    @Override
    public void showMessageNotAssessor() {
        snackBar.show();
    }

    @Override
    public void hideSnackbar() {
        snackBar.dismiss();
    }

    @Override
    public ChatAssessorClient getChat() {
        return chat;
    }

    @Override
    public void setChat(ChatAssessorClient chat) {
        this.chat = chat;
    }

    @Override
    public ChatFirebaseAdapter createAdapter(FirebaseRecyclerOptions<FashionMessage> options) {
        chatFirebaseAdapter = new ChatFirebaseAdapter(options, this);
        return chatFirebaseAdapter;
    }

    @Override
    public void initializeAdapter() {
        chatFirebaseAdapter.startListening();
        chatFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int count = chatFirebaseAdapter.getItemCount();
                int lastVisiblePosition = llm.findLastVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (count -1) && lastVisiblePosition == (positionStart - 1))) {
                    rvMessages.scrollToPosition(positionStart);
                }
            }
        });
        rvMessages.setAdapter(chatFirebaseAdapter);
        presenter.setCount();
    }

    @Override
    public void generateLayout() {
        llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        rvMessages.setLayoutManager(llm);
    }

    @Override
    public void startAdapter() {
        if (chatFirebaseAdapter != null)
            chatFirebaseAdapter.startListening();
    }

    @Override
    public void stopAdapter() {
        if (chatFirebaseAdapter != null)
            chatFirebaseAdapter.stopListening();
    }

    @Override
    public ChatFirebaseAdapter getAdapter() {
        return this.chatFirebaseAdapter;
    }

    @Override
    public void countVisible() {
        count.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCount(long remainingTime) {
        mCountDownTimer = new CountDownTimer(remainingTime + 1, 1000) {
            public void onTick(long millisUntilFinished) {
                int minutes;
                int seconds;
                String minString;
                String secString;
                mTimeUntilFinished = millisUntilFinished;

                long secondsUntilFinished = millisUntilFinished /1000;

                minutes = (int) (secondsUntilFinished/60);

                if (minutes < 10) {
                    minString = "0" + minutes;
                } else {
                    minString = Integer.toString(minutes);
                }

                seconds = (int) (secondsUntilFinished%60);

                if (seconds < 10) {
                    secString = "0" + seconds;
                } else {
                    secString = Long.toString(seconds);
                }
                count.setText(String.format("Tiempo restante: %s:%s", minString, secString));
            }

            public void onFinish() {
                finish();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        presenter.stopAdapterListening();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.startAdapterListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_assessor, menu);
        presenter.showMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_chat_assessor_show_contact:
                String emailClient = getChat().getClient();
                Intent intent = new Intent(this, ClientProfileActivity.class);
                intent.putExtra(CLIENT_EMAIL, emailClient);
                startActivity(intent);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUES_IMAGE_OPENABLE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Log.d(TAG, "Uri: " + uri.toString());

                    FashionMessage tempMessage = new FashionMessage(
                            null,
                            presenter.getUsername(),
                            null,
                            "img",
                            presenter.getCurrentDate(),
                            presenter.getEmailUser());
                    presenter.sendImageMessage(tempMessage, uri);
                }

            }
        }
    }

    @Override
    public void showProgress() {
        //
    }

    @Override
    public void hideProgress() {
        //
    }

    @Override
    public void onError(String error) {
        //
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        presenter.saveTimeRemaining(mTimeUntilFinished);
    }

}

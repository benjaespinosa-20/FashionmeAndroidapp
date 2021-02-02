package mx.app.fashionme.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.app.fashionme.R;
import mx.app.fashionme.pojo.FashionMessage;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.ui.modules.home.controller.HomeController;
import mx.app.fashionme.utils.Utils;

public class ChatActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = ChatActivity.class.getSimpleName();

    public static final String MESSAGES_CHILD = "messages";
    private static final int REQUEST_IMAGE = 2;
    public static final int MSG_LENGTH_LIMIT = 250;
    private static final String LOADING_IMAGE_URL = "data//";

    private String mUsername;
    private String mEmail;
    private String currentDate;

    private Button sendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar progressBar;
    private EditText mMessageEditText;
    private ImageView mAddMessageImageView;
    private Toolbar toolbar;

    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<FashionMessage, MessageViewHolder> mFirebaseAdapter;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (!SessionPrefs.get(this).isLoggedIn()) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, HomeController.class));
            finish();
            return;
        } else {
            mUsername   = SessionPrefs.get(this).getUserName();
            mEmail      = SessionPrefs.get(this).getEmail();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date();
            currentDate = formatter.format(date);
        }

        toolbar                     = findViewById(R.id.toolbar);

        setupToolbar();
        updateColors();

        progressBar                 = findViewById(R.id.progressBar);
        mMessageRecyclerView        = findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager        = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);

        mFirebaseDatabaseReference  = FirebaseDatabase.getInstance().getReference();

        SnapshotParser<FashionMessage> parser = new SnapshotParser<FashionMessage>() {
            @Override
            public FashionMessage parseSnapshot(DataSnapshot snapshot) {
                FashionMessage fashionMessage = snapshot.getValue(FashionMessage.class);
                if (fashionMessage != null) {
                    fashionMessage.setId(snapshot.getKey());
                }
                return fashionMessage;
            }
        };

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);

        FirebaseRecyclerOptions<FashionMessage> options =
                new  FirebaseRecyclerOptions.Builder<FashionMessage>()
                        .setQuery(messagesRef, parser)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<FashionMessage, MessageViewHolder>(options) {

            @Override
            public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new MessageViewHolder(inflater.inflate(R.layout.item_message, parent, false));
            }

            @Override
            protected void onBindViewHolder(final MessageViewHolder viewHolder,
                                            int position,
                                            FashionMessage fashionMessage) {

                progressBar.setVisibility(ProgressBar.INVISIBLE);
                if (fashionMessage.getText() != null) {
                    viewHolder.messageTextView.setText(fashionMessage.getText());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                } else if (fashionMessage.getImageUrl() != null) {
                    String imageUrl = fashionMessage.getImageUrl();

                    if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
//                        Glide.with(viewHolder.messageImageView.getContext())
//                                .load(imageUrl)
//                                .into(viewHolder.messageImageView);
                        Picasso.get().load(imageUrl).into(viewHolder.messageImageView);

                    } else {
//                        Glide.with(viewHolder.messageImageView.getContext())
//                                .load(R.raw.preloader)
//                                .into(viewHolder.messageImageView);
                        Picasso.get().load(R.drawable.logo_fme_black).into(viewHolder.messageImageView);

                        viewHolder.messageImageView.getLayoutParams().width = 350;
                        viewHolder.messageImageView.getLayoutParams().height = 350;
                    }

                    viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    viewHolder.messageTextView.setVisibility(TextView.GONE);
                }

                viewHolder.messengerTextView.setText(fashionMessage.getName());
                if (fashionMessage.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(ChatActivity.this,
                            R.drawable.ic_person_pin_black_24dp));
                } else {
                    Picasso.get()
                            .load(fashionMessage.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                int count = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (count - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mFirebaseAdapter);

        // Initializr Firebase Remote Config.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Define Firebase Remote Config Settings
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(true)
                    .build();

        // Define default config values. Defaults are used when fetched config values are not
        // available. EG: if an error occurred fetching values from server.
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put("friendly_msg_length", 250L);

        // Apply config settings and default values.
        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);

        // Fetch remote config
        fetchConfig();

        mMessageEditText = findViewById(R.id.messageEditText);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MSG_LENGTH_LIMIT)});
        mMessageEditText.addTextChangedListener(new TextWatcher() {
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

        mAddMessageImageView = findViewById(R.id.addMessageImageView);
        mAddMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FashionMessage fashionMessage = new FashionMessage(mMessageEditText.getText().toString(),
                        mUsername,
                        null,
                        null,
                        currentDate,
                        mEmail);
                mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(fashionMessage);
                mMessageEditText.setText("");
            }
        });

        if (mFirebaseAdapter.getItemCount() == 0)
            progressBar.setVisibility(View.GONE);

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString("screen_name", "Chat comunidad/Android");

        firebaseAnalytics.logEvent("open_screen", bundle);
    }

    private void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setBackgroundColor(Utils.getTabColor(this));
        }
    }

    private void updateColors() {

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
    protected void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void fetchConfig() {
        long cacheExpiration = 3600; // 1 hour in seconds
        // If developer mode is enabled reduce cacheExpiration to 0 so that each fetch goes to the
        // server. This should not be used in release builds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Make the fetched config available via FirebaseRemoteConfig get<type> calls.
                        mFirebaseRemoteConfig.activateFetched();
                        applyRetrievedLengthLimit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There has been an error fetching the config
                        Log.w(TAG, "Error fetching config", e);
                        applyRetrievedLengthLimit();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    Log.d(TAG, "Uri:" + uri.toString());

                    FashionMessage tempMessage = new FashionMessage(
                            null,
                            mUsername,
                            null,
                            LOADING_IMAGE_URL,
                            currentDate,
                            mEmail);
                    mFirebaseDatabaseReference
                            .child(MESSAGES_CHILD)
                            .push()
                            .setValue(tempMessage, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        String key = databaseReference.getKey();

                                        StorageReference storageReference =
                                                FirebaseStorage.getInstance()
                                                .getReference()
                                                .child("community_images")
                                                .child(uri.getLastPathSegment());

                                        putImageInStorage(storageReference, uri, key);
                                    } else {
                                        Log.w(TAG, "Unable to write message to database.", databaseError.toException());
                                    }
                                }
                            });
                }
            }
        }
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, String key) {
        storageReference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    FashionMessage message = new FashionMessage(
                            null,
                            mUsername,
                            null,
                            task.getResult().toString(),
                            currentDate,
                            mEmail
                    );
                    mFirebaseDatabaseReference.child(MESSAGES_CHILD).child(key).setValue(message);
                } else {
                    Log.w(TAG, "Image upload task was not successful.",
                            task.getException());
                }
            }
        });
    }

    private void applyRetrievedLengthLimit() {
        Long friendly_msg_length = mFirebaseRemoteConfig.getLong("friendly_msg_length");
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(friendly_msg_length.intValue())});
        Log.d(TAG, "FML is: " + friendly_msg_length);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        CircleImageView messengerImageView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView     = itemView.findViewById(R.id.messageTextView);
            messageImageView    = itemView.findViewById(R.id.messageImageView);
            messengerTextView   = itemView.findViewById(R.id.messengerTextView);
            messengerImageView  = itemView.findViewById(R.id.messengerImageView);
        }
    }


}

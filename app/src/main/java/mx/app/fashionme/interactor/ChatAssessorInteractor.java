package mx.app.fashionme.interactor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import mx.app.fashionme.data.database.entities.ChatEntity;
import mx.app.fashionme.data.repositories.DataBaseRepository;
import mx.app.fashionme.interactor.interfaces.IChatAssessorInteractor;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.pojo.FashionMessage;
import mx.app.fashionme.pojo.firebase.ChatClient;
import mx.app.fashionme.pojo.firebase.Message;
import mx.app.fashionme.pojo.firebase.User;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import mx.app.fashionme.view.ChatAssessorActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAssessorInteractor implements IChatAssessorInteractor {

    /**
     * Instancia de data base repo
     */
    private DataBaseRepository dataBaseRepository;
    /**
     * Instancia de listner
     */
    private IChatAssessorInteractor.ChatCallback mListener;

    private static final String TAG = "ChatAssesorInt";

    /**
     * Metodod con dependencias inyectadas
     *
     * @param dataBaseRepository instancia de database repo
     */
    @Inject
    public ChatAssessorInteractor(DataBaseRepository dataBaseRepository) {
        this.dataBaseRepository = dataBaseRepository;
    }


    @Override
    public void init(ChatCallback listener) {
        this.mListener = listener;
    }

    @Override
    public void getUID(Context context, ChatCallback callback) {

        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();

        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();
        Call<Base<ChatAssessorClient>> chatAssessorClientCall = endpointsApi.getUIDByUserId(userId);

        chatAssessorClientCall.enqueue(new Callback<Base<ChatAssessorClient>>() {
            @Override
            public void onResponse(Call<Base<ChatAssessorClient>> call, Response<Base<ChatAssessorClient>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            callback.onErrorRequest(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(ChatAssessorActivity.TAG, e.getMessage());
                            callback.onErrorRequest("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onErrorRequest(apiError != null ? apiError.getError():"Algo salió mal, intenta más tarde");
                        return;
                    }
                }
                callback.onGetUID(response.body().getData());
            }

            @Override
            public void onFailure(Call<Base<ChatAssessorClient>> call, Throwable t) {
                Log.e(ChatAssessorActivity.TAG, t.getMessage());
                t.printStackTrace();
                callback.onErrorRequest(t.getMessage());
            }
        });



    }

    @Override
    public DatabaseReference getInstanceFirebase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void pushChat(Context context, DatabaseReference firebaseInstance, ChatCallback callback) {
        ChatClient chatClient = new ChatClient(
                new Message(null, null, null, null, null, null),
                new User(SessionPrefs.get(context).getEmail(), null));
        firebaseInstance.child("chats").push().setValue(chatClient, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    callback.onCompleteRegister(databaseReference.getKey());
                } else {
                    Log.w(ChatAssessorActivity.TAG, "Unable to write message to database.", databaseError.toException());
                    callback.onErrorRequest("Ocurrio un error intenta mas tarde");
                }
            }
        });

    }

    @Override
    public void registerUIDF(Context context, String key, ChatCallback callback) {

        int userId = SessionPrefs.get(context).getUserId();

        RestApiAdapter apiAdapter = new RestApiAdapter();

        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<ChatAssessorClient>> chatAssessorClientCall = endpointsApi.registerUID(userId, new ChatClient(key));

        chatAssessorClientCall.enqueue(new Callback<Base<ChatAssessorClient>>() {
            @Override
            public void onResponse(Call<Base<ChatAssessorClient>> call, Response<Base<ChatAssessorClient>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            callback.onErrorRequest(response.errorBody().string());
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(ChatAssessorActivity.TAG, e.getMessage());
                            callback.onErrorRequest("Algo salió mal, intenta más tarde");
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        callback.onErrorRequest(apiError != null ? apiError.getError():"Algo salió mal, intenta más tarde");
                        return;
                    }
                }
                callback.onRegisterUID(response.body().getData());
            }

            @Override
            public void onFailure(Call<Base<ChatAssessorClient>> call, Throwable t) {
                Log.e(ChatAssessorActivity.TAG, t.getMessage());
                t.printStackTrace();
                callback.onErrorRequest(t.getMessage());
            }
        });



    }

    @Override
    public FirebaseRecyclerOptions<FashionMessage> getOptions(DatabaseReference messageRef, SnapshotParser<FashionMessage> parser) {
        return new FirebaseRecyclerOptions.Builder<FashionMessage>()
                .setQuery(messageRef, parser)
                .build();
    }

    @Override
    public SnapshotParser<FashionMessage> getParser() {
        return new SnapshotParser<FashionMessage>() {
            @Override
            public FashionMessage parseSnapshot(DataSnapshot snapshot) {
                FashionMessage message = snapshot.getValue(FashionMessage.class);
                if (message != null) {
                    message.setId(snapshot.getKey());
                }
                return message;
            }
        };
    }

    @Override
    public void sendMessageToFirebase(DatabaseReference firebaseInstance, String keyUid, FashionMessage message) {
        firebaseInstance.child("chats").child(keyUid).child("messages").push().setValue(message);
    }

    @Override
    public void sendImageMessageToFirebase(DatabaseReference firebaseInstance, String keyUID, Uri uri, FashionMessage tempMessage, Context context) {
        firebaseInstance.child("chats").child(keyUID).child("messages").push().setValue(tempMessage, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {

                    String keyMessage = databaseReference.getKey();
                    StorageReference storageReference =
                            FirebaseStorage.getInstance()
                            .getReference()
                            .child(keyUID)
                            .child(uri.getLastPathSegment());

                    putImageInStorage(storageReference, uri, keyUID, keyMessage, context);
                } else {
                    Log.w(TAG, "Unable to write message to database.",
                            databaseError.toException());
                }
            }
        });
    }

    private void putImageInStorage(StorageReference storageReference, Uri uri, String keyUID, String keyMessage, Context context) {
        storageReference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    FashionMessage message = new FashionMessage(
                            null,
                            getUsername(context),
                            null,
                            task.getResult().toString(),
                            getCurrentDate(),
                            getEmailUser(context));

                    getInstanceFirebase().child("chats").child(keyUID).child("messages").child(keyMessage).setValue(message);
                } else {
                    Log.w(TAG, "Image upload task was not successful.",
                            task.getException());
                }

            }
        });

    }

    private String getUsername(Context context) {
        return SessionPrefs.get(context).getUserName();
    }

    private String getCurrentDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        return format.format(date);
    }

    private String getEmailUser(Context context) {
        return SessionPrefs.get(context).getEmail();
    }

    /**
     * Metodo para obtener el tiempo restante
     */
    @Override
    public void getRemainingTime() {
        int updated = updateLastEntry();
        if (updated > 0) {
            getRemainingTimeFromDB();
        }
    }

    /**
     * Metodo para guardar nuevo tiempo restante
     * @param timeUntilFinished tiempo restante hasta que termine
     */
    @Override
    public void saveTimeRemaining(long timeUntilFinished) {
        dataBaseRepository.chatDao().updateTimeRemaining(timeUntilFinished);
    }

    /**
     * Metodo para actualizar entidad de chat
     *
     * @return columnas actualizadas
     */
    private int updateLastEntry() {
        return dataBaseRepository.chatDao().updateLastEntry(System.currentTimeMillis());
    }

    /**
     * Metodo para obtener el tiempo disponible de chat
     */
    private void getRemainingTimeFromDB() {
        long remainingTime = dataBaseRepository.chatDao().getRemainingTime();
        mListener.onGettingTime(remainingTime);
    }

}

package mx.app.fashionme.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.firebase.DeviceToken;
import mx.app.fashionme.prefs.SessionPrefs;
import mx.app.fashionme.restApi.EndpointsApi;
import mx.app.fashionme.restApi.adapter.RestApiAdapter;
import mx.app.fashionme.restApi.model.ApiError;
import mx.app.fashionme.restApi.model.Base;
import mx.app.fashionme.view.AssessorContactsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = ">>FirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: ");

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        //Log.d(TAG, "Send token to server: " + token);
        RestApiAdapter apiAdapter = new RestApiAdapter();

        EndpointsApi endpointsApi = apiAdapter.establecerConexionRestApi();

        Call<Base<DeviceToken>> call = endpointsApi.registerDeviceToken(new DeviceToken(token));

        call.enqueue(new Callback<Base<DeviceToken>>() {
            @Override
            public void onResponse(Call<Base<DeviceToken>> call, Response<Base<DeviceToken>> response) {
                if (!response.isSuccessful()) {
                    if (response.errorBody().contentType().type().equals("text")) {
                        try {
                            Log.d(TAG, response.errorBody().string());
                            Toast.makeText(MyFirebaseMessagingService.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            return;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(MyFirebaseMessagingService.this, "Algo salió mal", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        ApiError apiError = ApiError.fromResponseBody(response.errorBody());
                        Toast.makeText(MyFirebaseMessagingService.this, apiError != null ? apiError.getError():"Algo salió mal, intenta más tarde", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                SessionPrefs.get(getApplicationContext()).saveTokenAssociated(false);
            }

            @Override
            public void onFailure(Call<Base<DeviceToken>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                t.printStackTrace();
                Toast.makeText(MyFirebaseMessagingService.this, "Algo salió mal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, AssessorContactsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notification", "PAGE");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo_fme_black)
                .setContentTitle(getString(R.string.fme_message))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

}

package mx.app.fashionme.view;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mx.app.fashionme.R;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAB_ACTION = "action";

    private Button btnLoginNormal, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnLoginNormal  = findViewById(R.id.btnLoginNormal);
        btnRegister     = findViewById(R.id.btnRegister);

//        playVideo();

        btnLoginNormal.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

       switch (v.getId()) {
           case R.id.btnLoginNormal:
               intent = new Intent(StartActivity.this, LoginActivity.class);
               intent.putExtra(TAB_ACTION, 0);
               break;
           case R.id.btnRegister:
               intent = new Intent(StartActivity.this, LoginActivity.class);
               intent.putExtra(TAB_ACTION, 1);
               break;
       }

       startActivity(intent);
       StartActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //playVideo();
    }

    private void playVideo() {
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_video_2);
//
//        videoView.setVideoURI(uri);
//        videoView.start();
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
    }
}

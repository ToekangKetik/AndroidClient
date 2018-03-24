package etrash.datacake.project.kanaksasak.e_trash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import etrash.datacake.project.kanaksasak.e_trash.Service.FirebaseService;

public class SplashActivity extends AppCompatActivity {

    ImageView imglogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imglogo = findViewById(R.id.logo);

        startService(new Intent(this, FirebaseService.class));
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

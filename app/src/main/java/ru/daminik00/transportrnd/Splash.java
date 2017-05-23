package ru.daminik00.transportrnd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashh);

        ImageView imageView = (ImageView)findViewById(R.id.splash);

        Animation animZoomin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);

        imageView.startAnimation(animZoomin);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 2 * 1000);
    }
}

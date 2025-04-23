package com.example.autocheckouts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class Congrats extends AppCompatActivity {
    TextView t1,t2;
    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_congrats);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.yellow));

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        lottie = findViewById(R.id.lottie2);

        t1.animate().setDuration(2700).setStartDelay(0);
        t1.animate().setDuration(2700).setStartDelay(0);
        lottie.animate().setDuration(2000).setStartDelay(2900);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(Intro.this, Login.class);
//                startActivity(intent);
            }
        },2000);

    }
}
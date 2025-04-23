package SplashScreen;

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
import com.example.autocheckouts.Login;
import com.example.autocheckouts.MainActivity;
import com.example.autocheckouts.R;

public class Intro extends AppCompatActivity {
    TextView textView;
    LottieAnimationView lottie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_intro);

        
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.yellow));

        textView = findViewById(R.id.text2);
        lottie = findViewById(R.id.lottie);

        textView.animate().translationY(-1400).setDuration(2700).setStartDelay(0);
        lottie.animate().translationX(2000).setDuration(2000).setStartDelay(2900);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intro.this, Login.class);
                startActivity(intent);
            }
        },3000);

    }
}
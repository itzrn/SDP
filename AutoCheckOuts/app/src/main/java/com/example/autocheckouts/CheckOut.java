package com.example.autocheckouts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.reflect.TypeToken;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CheckOut extends AppCompatActivity {
    TextView name, amt;
    FirebaseAuth mAuth;
    private final Handler mHandler = new Handler();

    TextView curr_date,curr_time;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.yellow));

        name = findViewById(R.id.name);
        mAuth = FirebaseAuth.getInstance();
        amt = findViewById(R.id.total_amt);
        curr_time = findViewById(R.id.curr_time);
        curr_date = findViewById(R.id.curr_date);
        btn = findViewById(R.id.btn);

        String email = mAuth.getCurrentUser().getEmail();
        name.setText(email.substring(0, email.indexOf('@')));
        double total_price = 0.0d;

        String cartJson = getIntent().getStringExtra("cart_data");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> cart_item = gson.fromJson(cartJson, type);


        for (Map<String, Object> i : cart_item) {
            if (i.containsKey("itemname") && i.containsKey("itemprice") && i.containsKey("count")) {
                String itemname = String.valueOf(i.get("itemname"));
                double price = Double.parseDouble(String.valueOf(i.get("itemprice")));;
                int count = (int)Double.parseDouble(String.valueOf(i.get("count")));
                total_price += price * count;
                addItemCard(itemname, price, count);
            }
        }


        amt.setText("Total: ₹" + total_price);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        Date currentDate = new Date();

        // Set text to TextViews
        curr_date.setText(dateFormat.format(currentDate));
        curr_time.setText(timeFormat.format(currentDate));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckOut.this, Congrats.class);
                startActivity(intent);
            }
        });
    }


    @SuppressLint("SetTextI18n")
    public void addItemCard(String item_name, double price, int count) {
        LinearLayout linearLayout = findViewById(R.id.container);
        View view = getLayoutInflater().inflate(R.layout.table, null);

        TextView pro_name = view.findViewById(R.id.product_name);
        TextView pro_price = view.findViewById(R.id.price_count);

        pro_name.setText(item_name);
        pro_price.setText("₹" + price + "*" + count); // You can customize currency display

        linearLayout.addView(view);
    }
}

package Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import  androidx. appcompat. widget. Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autocheckouts.CheckOut;
import com.example.autocheckouts.Items;
import com.example.autocheckouts.MyAdapter;
import com.example.autocheckouts.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    Toolbar toolbar;

    private List<Items> list = new ArrayList<>();
    private RecyclerView rv;
    private MyAdapter adapter;
    ImageButton button;

    FirebaseAuth mAuth;

    DatabaseReference userRef;

    FloatingActionButton checkout;
    String email;
    String unique_id;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Items");


        rv = view.findViewById(R.id.recylerView);
        checkout = view.findViewById(R.id.floatingActionButton);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAuth = FirebaseAuth.getInstance();

        email = mAuth.getCurrentUser().getEmail();
        unique_id = email.substring(0, email.indexOf('@'));


        userRef = FirebaseDatabase.getInstance()
                .getReference("cart")
                .child(email.substring(0, email.indexOf('@')));

        Query lastTenQuery = userRef.orderByKey().limitToLast(10);

        lastTenQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();  // Clear old data

                Gson gson = new Gson();
                String json = gson.toJson(snapshot.getValue());
                System.out.println("FirebaseJSON"+json);  // Log JSON to Logcat

                try {
                    JSONObject jsonObject = new JSONObject(json);

                    Iterator<String> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String timestamp = keys.next();
                        JSONObject itemObject = jsonObject.getJSONObject(timestamp);

                        String itemName = itemObject.getString("itemname");
                        String itemPrice = itemObject.getString("itemprice");
                        int imageResource = getDrawableResourceId(getContext(), itemName);
                        list.add(new Items(itemName, itemPrice, imageResource));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new MyAdapter(list);
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                list.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Map<String, Object>> cart_item = moveCartToHistory(unique_id, list);

                if (cart_item == null || cart_item.isEmpty()) {
                    Toast.makeText(getActivity(), "Cart is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Gson gson = new Gson();
                String cartJson = gson.toJson(cart_item);

                Intent intent = new Intent(getActivity(), CheckOut.class);
                intent.putExtra("cart_data", cartJson);
                startActivity(intent);
            }
        });
        return view;
    }
    public List<Map<String, Object>> moveCartToHistory(String userId, List<Items> itemList) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userHistoryRef = database.child("history").child(userId);
        DatabaseReference cartRef = database.child("cart").child(userId);

        long timestamp = System.currentTimeMillis() / 1000;
        DatabaseReference timestampRef = userHistoryRef.child(String.valueOf(timestamp));

        List<Map<String, Object>> itemsToSave = new ArrayList<>();
        int totalPrice = 0;

        for (Items item : itemList) {
            try {
                int price = Integer.parseInt(item.getPrice());
                int count = item.getQuantity();
                int total = price * count;
                totalPrice += total;

                Map<String, Object> itemData = new HashMap<>();
                itemData.put("itemname", item.getName());
                itemData.put("itemprice", price);
                itemData.put("count", count);

                itemsToSave.add(itemData);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Optionally log this or inform the user
            }
        }

        // First, push item list under timestamp
        int finalTotalPrice = totalPrice;
        timestampRef.setValue(itemsToSave).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Then update total_price (using transaction to accumulate if needed)
                userHistoryRef.child("total_price").runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        Long current = currentData.getValue(Long.class);
                        if (current == null) current = 0L;
                        currentData.setValue(current + finalTotalPrice);
                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot snapshot) {
                        if (committed) {
                            // Finally delete cart
                            cartRef.removeValue().addOnSuccessListener(aVoid -> {
                                System.out.println("Firebase" + "✅ Cart cleared after moving to history.");
                            }).addOnFailureListener(e -> {
                                System.out.println("Firebase"+ "❌ Failed to clear cart: " + e.getMessage());
                            });
                        } else {
                            System.out.println("Firebase"+ "❌ Failed to update total_price: " + error.getMessage());
                        }
                    }
                });
            } else {
                System.out.println("Firebase"+ "❌ Failed to push items to timestamp: " + task.getException().getMessage());
            }
        });
        return itemsToSave;
    }



    public int getDrawableResourceId(String itemName) {

        String drawableName = itemName.toLowerCase().replace(" ", "_");

        int resId = getResources().getIdentifier(drawableName, "drawable", getContext().getPackageName());

        if (resId == 0) {
        }

        return resId;
    }

    public int getDrawableResourceId(Context context, String itemName) {
        String drawableName = itemName.toLowerCase().replace(" ", "_");
        return context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
    }
}




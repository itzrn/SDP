package Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.autocheckouts.HistoryAdapter;
import com.example.autocheckouts.Modal;

import com.example.autocheckouts.Modal;
import com.example.autocheckouts.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class History extends Fragment {
    Toolbar toolbar;
    private List<Modal> list = new ArrayList<Modal>();
    private RecyclerView rv;
    private HistoryAdapter adapter;
    FirebaseAuth mAuth;
    DatabaseReference historyRef;
    String email;
    String unique_id;

    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_history, container, false);
//        list = solve();
        mAuth = FirebaseAuth.getInstance();

        email = mAuth.getCurrentUser().getEmail();
        unique_id = email.substring(0, email.indexOf('@'));

        rv = root.findViewById(R.id.recylerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryAdapter(list);
        rv.setAdapter(adapter);

        historyRef = FirebaseDatabase.getInstance()
                .getReference("history").child(unique_id);

        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot timestampSnapshot : snapshot.getChildren()) {
                    String rawTimestamp = timestampSnapshot.getKey();
                    String formattedTimestamp = formatTimestamp(rawTimestamp);

                    for (DataSnapshot itemSnapshot : timestampSnapshot.getChildren()) {
                        Long count = itemSnapshot.child("count").getValue(Long.class);
                        String itemName = itemSnapshot.child("itemname").getValue(String.class);
                        Long itemPrice = itemSnapshot.child("itemprice").getValue(Long.class);

                        int imageResId = getDrawableResourceId(itemName); // from your method
                        Modal modal = new Modal(formattedTimestamp, itemName, String.valueOf(itemPrice), imageResId, count.intValue());

                        list.add(modal);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Firebase"+ "Database error: " + error.getMessage());
            }
        });
        return root;
    }

    private String formatTimestamp(String timestamp) {
        try {
            long timeInMillis = Long.parseLong(timestamp) * 1000; // convert to milliseconds
            Date date = new Date(timeInMillis);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.getDefault());
            return sdf.format(date);
        } catch (NumberFormatException e) {
            return "Invalid time";
        }
    }


    //image
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

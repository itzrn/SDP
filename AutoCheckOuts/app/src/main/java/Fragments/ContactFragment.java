package Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import  androidx. appcompat. widget. Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.autocheckouts.Coupon;
import com.example.autocheckouts.R;

public class ContactFragment extends Fragment {
    Toolbar toolbar;
//    CardView c1,c2,c3,c4;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact,container,false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle("Contact Support");

        CardView c1 = view.findViewById(R.id.c1);
        CardView c2 = view.findViewById(R.id.c2);
        CardView c3 = view.findViewById(R.id.c3);
        CardView c4 = view.findViewById(R.id.c4);

        //call log
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1800012567"));
                startActivity(intent);
            }
        });

        //mail
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto: abc@gmail.com"));
                startActivity(intent);
            }
        });

        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Coupon.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
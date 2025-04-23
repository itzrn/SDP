package com.example.autocheckouts;


import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.autocheckouts.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragments.ContactFragment;
import Fragments.HomeFragment;
import Fragments.History;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    BottomNavigationView bottomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        // Change status bar color
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.yellow));

        binding.bottomView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.homeId) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.historyId) {
                replaceFragment(new History());
            } else if (itemId == R.id.contact) {
                replaceFragment(new ContactFragment());
            }

            return true;
        });
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout,fragment);
        ft.commit();
    }
}